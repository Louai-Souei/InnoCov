package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.RouteDto;
import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.RouteBookingRepository;
import covoiturage.project.InnoCov.repository.RouteRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.RouteService;
import covoiturage.project.InnoCov.util.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final RouteBookingRepository routeBookingRepository;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<ApiResponse<String>> addRoute(RouteDto routeDto) {
        try {
            Route route = routeDto.convert();
            route.setDriver(authenticationService.getActiveUser());
            routeRepository.save(route);
            log.info("Route added successfully: {}", route);
            return ResponseEntity.ok(new ApiResponse<>(true, "Success", "Route added successfully."));
        } catch (Exception e) {
            log.error("Error while adding route: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error", "Failed to add route."));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<RouteDto> getAllRouteInformation(Integer routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));

        List<User> passengers = routeBookingRepository.findByRoute(route)
                .stream()
                .map(RouteBooking::getPassenger)
                .collect(Collectors.toList());

        RouteDto routeDto = new RouteDto(route, passengers);

        return ResponseEntity.ok(routeDto);
    }

    @Transactional
    @Override
    public ResponseEntity<List<RouteDto>> getAvailableRoutes() {
        try {
            List<Route> routes = routeRepository.findAvailableRoutesWithCapacity();
            List<RouteDto> routeDtos = routes.stream()
                    .map(route -> new RouteDto(
                            route,
                            route.getBookings().stream()
                                    .map(RouteBooking::getPassenger)
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(routeDtos);
        } catch (Exception e) {
            log.error("Error while fetching available routes: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }


}
