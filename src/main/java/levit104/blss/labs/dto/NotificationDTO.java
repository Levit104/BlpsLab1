package levit104.blss.labs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private String message;
    private LocalDateTime time;
}
