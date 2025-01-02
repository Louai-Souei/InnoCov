package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.RouteBookingRepository;
import covoiturage.project.InnoCov.repository.RouteRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteBookingServiceImpl implements RouteBookingService {

    private final RouteBookingRepository routeBookingRepository;
    private final RouteRepository routeRepository;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<ApiResponse<String>> addBooking(Integer routeId) {
        try {
            User activeUser = authenticationService.getActiveUser();
            Route route = routeRepository.findById(routeId)
                    .orElseThrow(() -> new IllegalArgumentException("Route not found"));

            long currentBookings = routeBookingRepository.countByRoute(route);
            if (currentBookings >= route.getNumberOfPassengers()) {
                log.warn("Route is fully booked: {}", route.getId());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Error", "No available spots on this route."));
            }

            RouteBooking routeBooking = new RouteBooking();
            routeBooking.setPassenger(activeUser);
            routeBooking.setRoute(route);
            routeBooking.setBookingDate(new Date());

            routeBookingRepository.save(routeBooking);
            log.info("Booking added successfully: {}", routeBooking);

            return ResponseEntity.ok(new ApiResponse<>(true, "Success", "Booking added successfully."));
        } catch (Exception e) {
            log.error("Error while adding booking: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error", "Failed to add booking."));
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





    /**
     * Annuler une réservation par un passager.
     */
    @Override
    public String cancelBooking(Integer bookingId, String passengerEmail) {
        // Vérifier si la réservation existe et appartient au passager
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        if (!booking.getPassenger().getEmail().equals(passengerEmail)) {
            throw new IllegalStateException("Cette réservation n'appartient pas à l'utilisateur.");
        }

        if ("cancelled".equals(booking.getStatus())) {
            return "La réservation a déjà été annulée.";
        }

        // Modifier le statut de la réservation
        booking.setStatus("cancelled");
        routeBookingRepository.save(booking);

        return "Réservation annulée avec succès.";
    }

    /**
     * Récupérer les réservations annulées d'un passager.
     */
    @Override
    public List<RouteBooking> getCancelledBookings(String passengerEmail) {
        return routeBookingRepository.findByPassengerEmailAndStatus(passengerEmail, "cancelled");
    }


    public List<RouteBooking> getBookingsByDriverEmail(String email) {
        return routeBookingRepository.findByDriverEmail(email);
    }
    @Override
    public RouteBooking acceptBooking(Integer bookingId) {
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
        booking.setStatus("accepted");
        return routeBookingRepository.save(booking);
    }

    @Override
    public RouteBooking rejectBooking(Integer bookingId) {
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with ID: " + bookingId));
        booking.setStatus("rejected");
        return routeBookingRepository.save(booking);
    }

    @Override
    public List<RouteBooking> getBookingsByRoute(Route route) {
        return routeBookingRepository.findAll();
    }

    @Override
    public void updateBookingStatus(Integer bookingId, String status) {
        RouteBooking booking = routeBookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
        booking.setStatus(status);
        routeBookingRepository.save(booking);
    }
}
