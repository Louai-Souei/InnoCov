package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    List<User> findAll();


    List<User> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
