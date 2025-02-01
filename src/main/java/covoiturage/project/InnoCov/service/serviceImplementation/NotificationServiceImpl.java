package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.entity.Notification;
import covoiturage.project.InnoCov.repository.NotificationRepository;
import covoiturage.project.InnoCov.service.serviceInterface.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public void sendNotification(String userId, Notification notification) {
        log.info("Sending WS notification to {} with payload {}", userId, notification);
        messagingTemplate.convertAndSendToUser(
                userId,
                "/notifications",
                notification
        );
        notificationRepository.save(notification);
    }
}