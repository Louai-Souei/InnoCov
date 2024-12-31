package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RouteBookingService {

   ResponseEntity<ApiResponse<String>> addBooking(Integer routeId);

   int getAvailableSeats(Integer routeId);

   ResponseEntity<ApiResponse<List<RouteDto>>> getAllBookedRoutesByActiveUser();
}
