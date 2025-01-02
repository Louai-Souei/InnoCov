package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.service.serviceInterface.RouteService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
