package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.tools.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteBookingDto implements Convertible<RouteBooking> {
    private Integer id;
    private UserDto passenger;
    private RouteDto route;
    private Date bookingDate;

    public RouteBookingDto(RouteBooking routeBooking) {
        this.id = routeBooking.getId();
        this.bookingDate = routeBooking.getBookingDate();
        if (routeBooking.getPassenger() != null) {
            this.passenger = new UserDto(routeBooking.getPassenger());
        }
        if (routeBooking.getRoute() != null) {
            this.route = new RouteDto(routeBooking.getRoute());
        }
    }

    @Override
    public RouteBooking convert() {
        RouteBooking routeBooking = new RouteBooking();
        routeBooking.setId(this.id);
        routeBooking.setBookingDate(this.bookingDate);

        if (this.passenger != null) {
            routeBooking.setPassenger(this.passenger.convert());
        }
        if (this.route != null) {
            routeBooking.setRoute(this.route.convert());
        }
        return routeBooking;
    }
}
