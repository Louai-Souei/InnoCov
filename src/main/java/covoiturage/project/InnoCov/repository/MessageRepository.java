package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByReceiverIdAndIsReadFalse(Integer receiverId);
    List<Message> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);
}
