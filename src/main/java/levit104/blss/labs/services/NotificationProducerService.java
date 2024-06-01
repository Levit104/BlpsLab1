package levit104.blss.labs.services;

import levit104.blss.labs.messaging.NotificationContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationProducerService {
    private final KafkaTemplate<String, NotificationContainer> producer;

    public void send(String topic, String message, List<String> usernames) {
        NotificationContainer notification = new NotificationContainer(message, usernames);
        producer.send(topic, notification);
    }

    public void send(String message, List<String> usernames) {
        send("notifications", message, usernames);
    }
}
