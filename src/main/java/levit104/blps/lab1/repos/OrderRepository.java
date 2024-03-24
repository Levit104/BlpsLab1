package levit104.blps.lab1.repos;

import levit104.blps.lab1.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndClient_Username(Long id, String clientUsername);

    Optional<Order> findByIdAndGuide_Username(Long id, String guideUsername);

    List<Order> findAllByClient_Username(String clientUsername);

    List<Order> findAllByGuide_Username(String guideUsername);
}
