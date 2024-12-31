package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route-booking")
@RequiredArgsConstructor
public class RouteBookingController {

    private final RouteBookingService routeBookingService;

    @PostMapping("/new-booking/{routeId}")
    public ResponseEntity<ApiResponse<String>> addBooking(@PathVariable Integer routeId) {
        return routeBookingService.addBooking(routeId);
    }

    @GetMapping("/available-seats/{routeId}")
    public int getAvailableSeats(@PathVariable Integer routeId) {
        return routeBookingService.getAvailableSeats(routeId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RouteBookingDto> changeBookingStatus(@PathVariable Integer id, @RequestParam String status) {
        RouteBookingDto updatedBooking = routeBookingService.changeBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }
    @GetMapping("/by-passenger-email")
    public ResponseEntity<List<RouteBookingDto>> getRouteBookingsByPassengerEmail(@RequestParam String email) {
        List<RouteBookingDto> routeBookings = routeBookingService.getRouteBookingsByPassengerEmail(email);
        return ResponseEntity.ok(routeBookings);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Integer id,
            @RequestParam String passengerEmail) {
        String message = routeBookingService.cancelBooking(id, passengerEmail);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/cancelled")
    public ResponseEntity<List<RouteBooking>> getCancelledBookings(
            @RequestParam String passengerEmail) {
        List<RouteBooking> bookings = routeBookingService.getCancelledBookings(passengerEmail);
        return ResponseEntity.ok(bookings);
    }
}
