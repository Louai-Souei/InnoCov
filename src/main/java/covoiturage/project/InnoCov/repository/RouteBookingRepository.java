package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteBookingRepository extends JpaRepository<RouteBooking, Integer> {

    int countByRoute(Route route);

    List<RouteBooking> findByRoute(Route route);
}