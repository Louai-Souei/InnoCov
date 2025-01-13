package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface RouteBookingService {

   ResponseEntity<ApiResponse<String>> addBooking(Integer routeId);

   int getAvailableSeats(Integer routeId);

   RouteBookingDto changeBookingStatus(Integer bookingId, String status);

   List<RouteBookingDto> getRouteBookingsByPassengerEmail(String email);

   String cancelBooking(Integer bookingId, String passengerEmail);

   List<RouteBooking> getBookingsByDriverEmail(String passengerEmail);

   List<RouteBooking> getCancelledBookings(String passengerEmail);

   List<RouteBookingDto> getBookingsByRoute(Route route);

   ResponseEntity<ApiResponse<Void>> updateBookingStatus(Integer bookingId, String status);

   ResponseEntity<ApiResponse<List<RouteDto>>> getAllBookedRoutesByActiveUser();

   Map<String, Long> getUserCreationStatsForLast4Weeks();

   Map<String, Long> getRouteBookingsCreatedStatsForLast4Weeks();
}
