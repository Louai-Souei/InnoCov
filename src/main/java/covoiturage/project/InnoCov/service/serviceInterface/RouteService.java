package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface RouteService {

    ResponseEntity<ApiResponse<String>> addRoute(RouteDto route);
}
