package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RouteBookingService {

   ResponseEntity<ApiResponse<String>> addBooking(Integer routeId);

   int getAvailableSeats(Integer routeId);
}
