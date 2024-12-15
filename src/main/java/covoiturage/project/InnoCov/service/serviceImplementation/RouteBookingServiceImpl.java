package covoiturage.project.InnoCov.service.serviceImplementation;

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

import java.util.Date;

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


}
