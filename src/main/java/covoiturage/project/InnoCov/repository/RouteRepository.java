package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import covoiturage.project.InnoCov.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByDriverEmail(String email);
    void deleteById(Integer id);


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
        AND (
            SELECT COUNT(b)
            FROM r.bookings b
            WHERE b.status = 'accepted'
        ) < r.numberOfPassengers
        ORDER BY r.departureDate ASC
    """)
    List<Route> findAvailableRoutesByDate(
            Date startOfDay,
            Date endOfDay);

    @Query("SELECT DISTINCT r.driver FROM Route r WHERE r.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    List<User> findUsersWhoCreatedRoutesThisWeek(@Param("startOfWeek") Date startOfWeek, @Param("endOfWeek") Date endOfWeek);

    @Query("SELECT COUNT(DISTINCT r.driver.id) " +
            "FROM Route r " +
            "WHERE r.createdAt BETWEEN :startDate AND :endDate")
    Long countUsersWhoCreatedRoutesBetween(Date startDate, Date endDate);

    @Query("SELECT COUNT(r.id) " +
            "FROM Route r " +
            "WHERE r.createdAt BETWEEN :startDate AND :endDate")
    Long countRoutesCreatedBetween(Date startDate, Date endDate);


}
