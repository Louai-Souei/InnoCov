package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.entity.Message;
import covoiturage.project.InnoCov.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl {

    private final MessageRepository messageRepository;

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getUserMessages(Integer userId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, userId);
    }

    public List<Message> getUnreadMessages(Integer userId) {
        return messageRepository.findByReceiverIdAndIsReadFalse(userId);
    }
}
