package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.entity.Notification;

public interface NotificationService {

    void sendNotification(String userId, Notification notification);

}
