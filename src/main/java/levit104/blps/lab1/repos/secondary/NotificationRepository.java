package levit104.blps.lab1.repos.secondary;

import levit104.blps.lab1.models.secondary.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
