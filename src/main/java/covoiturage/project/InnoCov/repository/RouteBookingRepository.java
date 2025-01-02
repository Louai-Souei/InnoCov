package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteBookingRepository extends JpaRepository<RouteBooking, Integer> {

    int countByRoute(Route route);

    List<RouteBooking> findAll();

    List<RouteBooking> findByRoute(Route route);
    List<RouteBooking> findByPassengerEmail(String email);
    List<RouteBooking> findByPassengerEmailAndStatus(String email, String status);
    @Query("SELECT rb.id FROM RouteBooking rb " +
            "JOIN rb.route r " +
            "JOIN r.driver d " +
            "WHERE d.email = :email")
    List<RouteBooking> findByDriverEmail(@Param("email") String email);
    List<RouteBooking> findByRoute_IdAndStatus(Integer routeId, String status);


}