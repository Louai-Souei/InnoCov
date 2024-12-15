package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.tools.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Convertible<Route> {
    private Integer id;
    private String departure;
    private String arrival;
    private Date departureDate;
    private int numberOfPassengers;
    private UserDto driver;

    public RouteDto(Route route) {
        this.id = route.getId();
        this.departure = route.getDeparture();
        this.arrival = route.getArrival();
        this.departureDate = route.getDepartureDate();
        this.driver = new UserDto(route.getDriver());
    }

    @Override
    public Route convert() {
        Route route = new Route();
        route.setId(this.id);
        route.setDeparture(this.departure);
        route.setArrival(this.arrival);
        route.setDepartureDate(this.departureDate);
        if (this.driver != null)
            route.setDriver(this.driver.convert());
        return route;
    }
}