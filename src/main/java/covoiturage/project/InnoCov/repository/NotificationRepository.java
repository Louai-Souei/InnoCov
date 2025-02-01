package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
