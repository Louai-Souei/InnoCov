package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    @Query("""
                SELECT r 
                FROM Route r 
                WHERE r.departureDate > CURRENT_TIMESTAMP 
                AND SIZE(r.bookings) < r.numberOfPassengers 
                ORDER BY r.departureDate ASC
            """)
    List<Route> findAvailableRoutesWithCapacity();

    @Query("""
            SELECT r 
            FROM Route r 
            WHERE r.departureDate >= :startOfDay
            AND r.departureDate < :endOfDay
            AND SIZE(r.bookings) < r.numberOfPassengers 
            ORDER BY r.departureDate ASC
        """)
    List<Route> findAvailableRoutesByDate(
            Date startOfDay,
            Date endOfDay);


}
