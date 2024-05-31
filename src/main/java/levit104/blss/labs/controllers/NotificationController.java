package levit104.blss.labs.controllers;

import levit104.blss.labs.dto.NotificationDTO;
import levit104.blss.labs.models.secondary.Notification;
import levit104.blss.labs.services.NotificationService;
import levit104.blss.labs.utils.MappingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final MappingHelper mappingHelper;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN') && principal.username == #username")
    @GetMapping("/users/{username}/notifications")
    public List<NotificationDTO> showNotifications(
            @PathVariable String username,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<Notification> notifications = notificationService.getAllByUsername(username, pageable);
        return mappingHelper.mapList(notifications, NotificationDTO.class);
    }
}
