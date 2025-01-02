package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RouteBookingService {

   ResponseEntity<ApiResponse<String>> addBooking(Integer routeId);

   int getAvailableSeats(Integer routeId);
   RouteBookingDto changeBookingStatus(Integer bookingId, String status);
   List<RouteBookingDto> getRouteBookingsByPassengerEmail(String email);

   String cancelBooking(Integer bookingId, String passengerEmail);


   List<RouteBooking> getBookingsByDriverEmail(String passengerEmail);
   List<RouteBooking> getCancelledBookings(String passengerEmail);
   RouteBooking acceptBooking(Integer bookingId);
   RouteBooking rejectBooking(Integer bookingId);
   List<RouteBooking> getBookingsByRoute(Route route) ;
   void updateBookingStatus(Integer bookingId, String status);
}
