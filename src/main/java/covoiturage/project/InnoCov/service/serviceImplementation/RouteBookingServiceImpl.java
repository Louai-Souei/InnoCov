package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.RouteBookingRepository;
import covoiturage.project.InnoCov.repository.RouteRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.EmailServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteBookingServiceImpl implements RouteBookingService {

    private final RouteBookingRepository routeBookingRepository;
    private final RouteRepository routeRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final EmailServiceImpl emailService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<ApiResponse<String>> addBooking(Integer routeId) {
        try {
            User activeUser = authenticationService.getActiveUser();
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new IllegalArgumentException("Route not found"));
            long currentBookings = routeBookingRepository.countAcceptedByRoute(route);
            if (currentBookings >= route.getNumberOfPassengers()) {
                log.warn("Route is fully booked: {}", route.getId());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "No available spots on this route."));
            }

            RouteBooking routeBooking = new RouteBooking();
            routeBooking.setPassenger(activeUser);
            routeBooking.setRoute(route);
            routeBooking.setBookingDate(new Date());
            routeBookingRepository.save(routeBooking);

            emailService.sendRejectReservationEmail(
                    route.getDriver().getEmail(),
                    route.getDriver().getFirstname(),
                    route.getDriver().getLastname(),
                    route
            );

            log.info("Booking added successfully: {}", routeBooking);

            return ResponseEntity.ok(new ApiResponse<>(true, "Booking added successfully."));
        } catch (Exception e) {
            log.error("Error while adding booking: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Failed to add booking."));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public int getAvailableSeats(Integer routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route with ID " + routeId + " not found"));

        int bookedSeats = routeBookingRepository.countByRoute(route);
        int availableSeats = route.getNumberOfPassengers() - bookedSeats;
        return Math.max(availableSeats, 0);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<ApiResponse<List<RouteDto>>> getAllBookedRoutesByActiveUser() {
        try {
            User activeUser = authenticationService.getActiveUser();

            List<RouteBooking> bookings = routeBookingRepository.findByPassenger(activeUser);

            List<RouteDto> routeDtos = bookings.stream()
                    .map(routeBooking -> new RouteDto(routeBooking.getRoute(), routeBooking.getRoute().getBookings().stream()
                            .map(RouteBooking::getPassenger)
                            .collect(Collectors.toList())))
                    .sorted(Comparator.comparing(RouteDto::getDepartureDate))
                    .collect(Collectors.toList());

            log.info("Found {} routes for active user", routeDtos.size());

            return ResponseEntity.ok(new ApiResponse<>(true, "Routes fetched successfully.", routeDtos));
        } catch (Exception e) {
            log.error("Error while fetching routes for active user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Failed to fetch routes."));
        }
    }

    @Override
    public RouteBookingDto changeBookingStatus(Integer bookingId, String status) {
        RouteBooking routeBooking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("RouteBooking with ID " + bookingId + " not found"));

        if (!status.equals("accepted") && !status.equals("refused") && !status.equals("default")) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        routeBooking.setStatus(status);
        routeBookingRepository.save(routeBooking);
        return new RouteBookingDto(routeBooking);
    }


    @Override
    public List<RouteBookingDto> getRouteBookingsByPassengerEmail(String email) {
        List<RouteBooking> routeBookings = routeBookingRepository.findByPassengerEmail(email);
        return routeBookings.stream()
                .map(RouteBookingDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public String cancelBooking(Integer bookingId, String passengerEmail) {
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        if (!booking.getPassenger().getEmail().equals(passengerEmail)) {
            throw new IllegalStateException("Cette réservation n'appartient pas à l'utilisateur.");
        }

        if ("cancelled".equals(booking.getStatus())) {
            return "La réservation a déjà été annulée.";
        }

        booking.setStatus("cancelled");
        routeBookingRepository.save(booking);

        return "Réservation annulée avec succès.";
    }

    @Override
    public List<RouteBooking> getCancelledBookings(String passengerEmail) {
        return routeBookingRepository.findByPassengerEmailAndStatus(passengerEmail, "cancelled");
    }


    public List<RouteBooking> getBookingsByDriverEmail(String email) {
        return routeBookingRepository.findByDriverEmail(email);
    }

    @Override
    public List<RouteBookingDto> getBookingsByRoute(Route route) {
        return routeBookingRepository.findAll().stream()
                .map(RouteBookingDto::new)
                .toList();
    }
    @Override
    public ResponseEntity<ApiResponse<Void>> updateBookingStatus(Integer bookingId, String status) {
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if ("accepted".equalsIgnoreCase(status)) {
            long currentBookings = routeBookingRepository.countAcceptedByRoute(booking.getRoute());
            if (currentBookings >= booking.getRoute().getNumberOfPassengers()) {
                log.warn("Route {} is fully booked. Booking {} cannot be accepted.",
                        booking.getRoute().getId(), bookingId);
                ApiResponse<Void> response = new ApiResponse<>(
                        false,
                        "Booking Denied",
                        "The route is fully booked. Unable to accept this booking."
                );
                return ResponseEntity.ok(response);
            }

            booking.setStatus(status);
            routeBookingRepository.save(booking);

            emailService.sendAcceptReservationEmail(
                    booking.getPassenger().getEmail(),
                    booking.getPassenger().getFirstname(),
                    booking.getPassenger().getLastname(),
                    booking.getRoute()
            );

            log.info("Booking {} has been accepted for route {}.", bookingId, booking.getRoute().getId());
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    "Booking Accepted",
                    "The booking has been successfully accepted."
            );
            return ResponseEntity.ok(response);
        }

        if ("rejected".equalsIgnoreCase(status)) {
            booking.setStatus(status);
            routeBookingRepository.save(booking);

            emailService.sendRejectReservationEmail(
                    booking.getPassenger().getEmail(),
                    booking.getPassenger().getFirstname(),
                    booking.getPassenger().getLastname(),
                    booking.getRoute()
            );

            log.info("Booking {} has been rejected for route {}.", bookingId, booking.getRoute().getId());
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    "Booking Rejected",
                    "The booking has been successfully rejected."
            );
            return ResponseEntity.ok(response);
        }

        log.warn("Unknown status '{}' for booking {}. No action taken.", status, bookingId);
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Invalid Status",
                "The provided status is invalid. No action has been taken."
        );
        return ResponseEntity.ok(response);
    }



    @Transactional
    @Override
    public Map<String, Long> getUserCreationStatsForLast4Weeks() {
        try {
            LocalDate startDate = LocalDate.now().minusWeeks(4);
            LocalDate endDate = LocalDate.now().plusDays(1);

            Map<String, Long> routeStats = new TreeMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 4; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = (i == 3)
                        ? weekStart.plusDays(7).minusDays(1)
                        : weekStart.plusDays(6);

                Date start = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

                Long count = routeBookingRepository.countUsersWhoCreatedRoutebookingsBetween(start, end);

                String dateRangeKey = weekStart.format(formatter) + " To " + weekEnd.format(formatter);
                routeStats.put(dateRangeKey, count);
            }

            log.info("User creation stats for the last 4 weeks: {}", routeStats);
            return routeStats;
        } catch (Exception e) {
            log.error("Error fetching user creation stats: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user creation stats.");
        }
    }

    @Transactional
    @Override
    public Map<String, Long> getRouteBookingsCreatedStatsForLast4Weeks() {
        try {
            LocalDate startDate = LocalDate.now().minusWeeks(4);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Map<String, Long> weeklyStats = new TreeMap<>();

            for (int i = 0; i < 4; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = (i == 3)
                        ? weekStart.plusDays(7).minusDays(1)
                        : weekStart.plusDays(6);

                Date start = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

                Long count = routeBookingRepository.countRouteBookingsCreatedBetween(start, end);

                String dateRangeKey = weekStart.format(formatter) + " To " + weekEnd.format(formatter);

                weeklyStats.put(dateRangeKey, count);
            }

            log.info("Routes created stats for the last 4 weeks: {}", weeklyStats);
            return weeklyStats;
        } catch (Exception e) {
            log.error("Error fetching routes created stats for the last 4 weeks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch routes created stats.");
        }
    }

}
