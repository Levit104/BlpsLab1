package levit104.blss.labs.services;

import levit104.blss.labs.messaging.NotificationContainer;
import levit104.blss.labs.models.secondary.Notification;
import levit104.blss.labs.repos.secondary.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumerService {
    private final NotificationRepository notificationRepository;

    public List<Notification> getAllByUsername(String username, Pageable pageable) {
        return notificationRepository.findAllByUsername(username, pageable);
    }

    @KafkaListener(topics = "notifications")
    @Transactional
    public void receiveNotification(NotificationContainer notification) {
        log.info("Получено сообщение для {}: {}", notification.getUsernames(), notification.getMessage());
        handleNotification(notification);
    }

    private void handleNotification(NotificationContainer notification) {
        List<Notification> notifications = notification.getUsernames()
                .stream()
                .map(username -> createNotification(notification.getMessage(), username))
                .toList();
        notificationRepository.saveAll(notifications);
    }

    private Notification createNotification(String message, String username) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUsername(username);
        notification.setTime(LocalDateTime.now());
        return notification;
    }
}
