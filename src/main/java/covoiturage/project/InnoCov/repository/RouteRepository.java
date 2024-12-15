package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Integer> {
}
