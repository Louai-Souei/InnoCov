package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByDriverEmail(String email);
    void deleteById(Integer id);

}
