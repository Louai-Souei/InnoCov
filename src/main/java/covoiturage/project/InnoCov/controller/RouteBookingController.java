package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.service.serviceInterface.RouteBookingService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
