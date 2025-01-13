package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.RouteBookingDto;
import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
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
    @PreAuthorize("hasRole('ADMIN')")
    public int getAvailableSeats(@PathVariable Integer routeId) {
        return routeBookingService.getAvailableSeats(routeId);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<RouteBookingDto> changeBookingStatus(@PathVariable Integer id, @RequestParam String status) {
        RouteBookingDto updatedBooking = routeBookingService.changeBookingStatus(id, status);
        return ResponseEntity.ok(updatedBooking);
    }
    @GetMapping("/by-passenger-email")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<RouteBookingDto>> getRouteBookingsByPassengerEmail(@RequestParam String email) {
        List<RouteBookingDto> routeBookings = routeBookingService.getRouteBookingsByPassengerEmail(email);
        return ResponseEntity.ok(routeBookings);
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('DRIVER')")
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


    @GetMapping("/driver/{email}")
    public ResponseEntity<List<RouteBooking>> getBookingsByDriverEmail(@PathVariable String email) {
        List<RouteBooking> bookings = routeBookingService.getBookingsByDriverEmail(email);
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/by-route/{routeId}")
    public List<RouteBookingDto> getBookingsByRoute(@PathVariable Integer routeId) {
        Route route = new Route();
        route.setId(routeId);
        return routeBookingService.getBookingsByRoute(route);
    }

    @PutMapping("/{bookingId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptBooking(@PathVariable Integer bookingId) {
        return routeBookingService.updateBookingStatus(bookingId, "accepted");
    }

    @PutMapping("/{bookingId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectBooking(@PathVariable Integer bookingId) {
        return routeBookingService.updateBookingStatus(bookingId, "rejected");
    }

    @GetMapping("/routes-booked")
    public ResponseEntity<ApiResponse<List<RouteDto>>> getUserBookedRoutes() {
        return routeBookingService.getAllBookedRoutesByActiveUser();
    }

    @GetMapping("/user-creation-stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUserCreationStatsForLast4Weeks() {
        try {
            log.info("Fetching routes created stats for the last 4 weeks");
            Map<String, Long> stats = routeBookingService.getUserCreationStatsForLast4Weeks();
            return ResponseEntity.ok(new ApiResponse<>(true, "Active Passengers creation stats fetched successfully.", stats));
        } catch (Exception e) {
            log.error("Error fetching routes created stats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch Active Passengers stats."));
        }
    }

    @GetMapping("/route-bookings-creation-stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRoutesCreatedStatsForLast4Weeks() {
        try {
            log.info("Fetching routes created stats for the last 4 weeks");
            Map<String, Long> stats = routeBookingService.getRouteBookingsCreatedStatsForLast4Weeks();
            return ResponseEntity.ok(new ApiResponse<>(true, "Route bookings creation stats fetched successfully.", stats));
        } catch (Exception e) {
            log.error("Error fetching routes created stats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch Route bookings creation stats."));
        }
    }

}
