package levit104.blss.labs.services;

import levit104.blss.labs.models.secondary.Notification;
import levit104.blss.labs.repos.secondary.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotification(String message, String username) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUsername(username);
        notification.setTime(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}
