package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.service.serviceInterface.RouteService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
@RequiredArgsConstructor

public class RouteController {

    private final RouteService routeService;

    @PostMapping("/new-route")
    public ResponseEntity<ApiResponse<String>> addRoute(@RequestBody RouteDto route) {
        return routeService.addRoute(route);
    }

}
