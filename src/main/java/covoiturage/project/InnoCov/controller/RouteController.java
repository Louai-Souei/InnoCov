package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.service.serviceInterface.RouteService;
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
@RequestMapping("/route")
@RequiredArgsConstructor

public class RouteController {

    private final RouteService routeService;

    @PostMapping("/new-route")
    public ResponseEntity<ApiResponse<String>> addRoute(@RequestBody RouteDto route) {
        return routeService.addRoute(route);
    }

    @GetMapping("/route-information/{routeId}")
    public ResponseEntity<RouteDto> getAllRouteInformations(@PathVariable Integer routeId) {
        return routeService.getAllRouteInformation(routeId);
    }
    @GetMapping("/driver-routes/{email}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<RouteDto>> getRoutesByDriverEmail(@PathVariable String email) {
        return routeService.getRoutesByDriverEmail(email);
    }
    @PutMapping("/updateRoute/{id}")
    public ResponseEntity<RouteDto> updateRoute(@PathVariable Integer id, @RequestBody RouteDto routeDto) {
        RouteDto updatedRoute = routeService.updateRoute(id, routeDto);
        return ResponseEntity.ok(updatedRoute);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Integer id) {
        boolean isDeleted = routeService.deleteRouteById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(404).body("Route not found with ID: " + id);
    }

    @GetMapping("/available")
    public ResponseEntity<List<RouteDto>> getAvailableRoutes(@RequestParam(
            name = "date",
            required = false) String date
    ) {
        return routeService.getAvailableRoutes(date);
    }

    @GetMapping("/user-creation-stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUserCreationStatsForLast4Weeks() {
        try {
            log.info("Fetching user creation stats for the last 4 weeks");
            Map<String, Long> stats = routeService.getUserCreationStatsForLast4Weeks();
            return ResponseEntity.ok(new ApiResponse<>(true, "Active Drivers stats fetched successfully.", stats));
        } catch (Exception e) {
            log.error("Error fetching user creation stats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetchActive Driver stats."));
        }
    }

    @GetMapping("/routes-creation-stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getRoutesCreatedStatsForLast4Weeks() {
        try {
            log.info("Fetching routes created stats for the last 4 weeks");
            Map<String, Long> stats = routeService.getRoutesCreatedStatsForLast4Weeks();
            return ResponseEntity.ok(new ApiResponse<>(true, "Routes creation stats fetched successfully.", stats));
        } catch (Exception e) {
            log.error("Error fetching routes created stats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch routes creation stats."));
        }
    }


}
