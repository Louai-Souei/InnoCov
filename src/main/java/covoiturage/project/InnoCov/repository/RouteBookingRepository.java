package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.RouteBooking;
import covoiturage.project.InnoCov.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RouteBookingRepository extends JpaRepository<RouteBooking, Integer> {

    int countByRoute(Route route);

    List<RouteBooking> findAll();

    @Query("""
            SELECT COUNT(DISTINCT rb.id)
            FROM RouteBooking rb
            WHERE rb.route = :route
            AND rb.status = "accepted"
            """)
    int countAcceptedByRoute(Route route);

    @Query("""
            SELECT rb FROM RouteBooking rb
            WHERE rb.route = :route
            AND rb.status = "accepted"
            """)
    List<RouteBooking> findAcceptedByRoute(Route route);

    List<RouteBooking> findByPassengerEmail(String email);

    List<RouteBooking> findByPassengerEmailAndStatus(String email, String status);

    @Query("SELECT rb.id FROM RouteBooking rb " +
            "JOIN rb.route r " +
            "JOIN r.driver d " +
            "WHERE d.email = :email")
    List<RouteBooking> findByDriverEmail(@Param("email") String email);

    List<RouteBooking> findByRoute_IdAndStatus(Integer routeId, String status);



    List<RouteBooking> findByPassenger(User passenger);

    @Query("SELECT COUNT(DISTINCT rb.passenger) " +
            "FROM RouteBooking rb " +
            "WHERE rb.bookingDate BETWEEN :startDate AND :endDate")
    Long countUsersWhoCreatedRoutebookingsBetween(Date startDate, Date endDate);

    @Query("SELECT COUNT(rb.id) " +
            "FROM RouteBooking rb " +
            "WHERE rb.bookingDate BETWEEN :startDate AND :endDate")
    Long countRouteBookingsCreatedBetween(Date startDate, Date endDate);

}