package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.RouteBookingRepository;
import covoiturage.project.InnoCov.repository.RouteRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
                        .body(new ApiResponse<>(false, "No available spots on this route."));
            }

            RouteBooking routeBooking = new RouteBooking();
            routeBooking.setPassenger(activeUser);
            routeBooking.setRoute(route);
            routeBooking.setBookingDate(new Date());

            routeBookingRepository.save(routeBooking);
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
}
