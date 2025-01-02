package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RouteService {

    ResponseEntity<ApiResponse<String>> addRoute(RouteDto route);

    ResponseEntity<RouteDto> getAllRouteInformation(Integer routeId);
    ResponseEntity<List<RouteDto>> getRoutesByDriverEmail(String email);

    ResponseEntity<List<RouteDto>> getAvailableRoutes(String date);


}
