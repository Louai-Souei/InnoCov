package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.tools.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Convertible<Route> {
    private Integer id;
    private String departure;
    private String arrival;
    private Date departureDate;
    private Date createdAt;
    private int numberOfPassengers;
    private UserDto driver;
    private List<UserDto> passengers;

    public RouteDto(Route route) {
        this.id = route.getId();
        this.departure = route.getDeparture();
        this.arrival = route.getArrival();
        this.departureDate = route.getDepartureDate();
        this.numberOfPassengers = route.getNumberOfPassengers();
        this.driver = new UserDto(route.getDriver());
    }

    public RouteDto(Route route, List<User> passengers) {
        this.id = route.getId();
        this.departure = route.getDeparture();
        this.arrival = route.getArrival();
        this.departureDate = route.getDepartureDate();
        this.numberOfPassengers = route.getNumberOfPassengers();

        this.createdAt = route.getCreatedAt();
        this.driver = new UserDto(route.getDriver());
        this.passengers = passengers.stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public Route convert() {
        Route route = new Route();
        route.setId(this.id);
        route.setDeparture(this.departure);
        route.setArrival(this.arrival);
        route.setDepartureDate(this.departureDate);
        route.setNumberOfPassengers(this.numberOfPassengers);
        if (this.driver != null)
            route.setDriver(this.driver.convert());
        return route;
    }
}