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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        List<User> passengers = routeBookingRepository.findAcceptedByRoute(route)
                .stream()
                .map(RouteBooking::getPassenger)
                .collect(Collectors.toList());

        RouteDto routeDto = new RouteDto(route, passengers);

        return ResponseEntity.ok(routeDto);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<List<RouteDto>> getRoutesByDriverEmail(String email) {
        try {
            List<Route> routes = routeRepository.findByDriverEmail(email);

            if (routes.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            List<RouteDto> routeDtos = routes.stream()
                    .map(route -> {
                        List<User> passengers = routeBookingRepository.findAcceptedByRoute(route)
                                .stream()
                                .map(RouteBooking::getPassenger)
                                .collect(Collectors.toList());

                        return new RouteDto(route, passengers);
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(routeDtos);
        } catch (Exception e) {
            log.error("Error fetching routes for driver with email {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Override
    public RouteDto updateRoute(Integer id, RouteDto routeDto) {
        log.info("Updating route: {}", id.toString());
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route with ID " + id + " not found"));

        if (routeDto.getDeparture() != null) route.setDeparture(routeDto.getDeparture());
        if (routeDto.getArrival() != null) route.setArrival(routeDto.getArrival());
        if (routeDto.getDepartureDate() != null) route.setDepartureDate(routeDto.getDepartureDate());
        if (routeDto.getNumberOfPassengers() > 0) route.setNumberOfPassengers(routeDto.getNumberOfPassengers());



        Route updatedRoute = routeRepository.save(route);
        return new RouteDto(updatedRoute);
    }


    @Override
    public boolean deleteRouteById(Integer id) {
        if (routeRepository.existsById(id)) {
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public ResponseEntity<List<RouteDto>> getAvailableRoutes(String date) {
        try {
            List<Route> routes;
            if (date != null) {
                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH));

                Date startOfDay = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date endOfDay = Date.from(localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

                routes = routeRepository.findAvailableRoutesByDate(startOfDay, endOfDay);
            } else {
                routes = routeRepository.findAvailableRoutesWithCapacity();
            }

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

    @Transactional
    @Override
    public Map<String, Long> getUserCreationStatsForLast4Weeks() {
        try {
            LocalDate startDate = LocalDate.now().minusWeeks(4);
            LocalDate endDate = LocalDate.now().plusDays(1);

            Map<String, Long> routeStats = new TreeMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 4; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = (i == 3)
                        ? weekStart.plusDays(7).minusDays(1)
                        : weekStart.plusDays(6);

                Date start = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

                Long count = routeRepository.countUsersWhoCreatedRoutesBetween(start, end);

                String dateRangeKey = weekStart.format(formatter) + " To " + weekEnd.format(formatter);
                routeStats.put(dateRangeKey, count);
            }

            log.info("User creation stats for the last 4 weeks: {}", routeStats);
            return routeStats;
        } catch (Exception e) {
            log.error("Error fetching user creation stats: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user creation stats.");
        }
    }

    @Transactional
    @Override
    public Map<String, Long> getRoutesCreatedStatsForLast4Weeks() {
        try {
            LocalDate startDate = LocalDate.now().minusWeeks(4);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Map<String, Long> weeklyStats = new TreeMap<>();

            for (int i = 0; i < 4; i++) {
                LocalDate weekStart = startDate.plusWeeks(i);
                LocalDate weekEnd = (i == 3)
                        ? weekStart.plusDays(7).minusDays(1)
                        : weekStart.plusDays(6);

                Date start = Date.from(weekStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(weekEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

                Long count = routeRepository.countRoutesCreatedBetween(start, end);
                String dateRangeKey = weekStart.format(formatter) + " To " + weekEnd.format(formatter);

                weeklyStats.put(dateRangeKey, count);
            }

            log.info("Routes created stats for the last 4 weeks: {}", weeklyStats);
            return weeklyStats;
        } catch (Exception e) {
            log.error("Error fetching routes created stats for the last 4 weeks: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch routes created stats.");
        }
    }



}
