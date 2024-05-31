package levit104.blss.labs.repos.secondary;

import levit104.blss.labs.models.secondary.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUsername(String username, Pageable pageable);
}
