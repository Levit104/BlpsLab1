package levit104.blss.labs.repos.primary;

import levit104.blss.labs.models.primary.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(
            "select o from Order o " +
            "left join fetch o.client cl " +
            "left join fetch o.guide g " +
            "left join fetch o.tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch o.status s " +
            "where o.id = ?1 and cl.username = ?2"
    )
    Optional<Order> findByIdAndClient_Username(Long id, String clientUsername);

    @Query(
            "select o from Order o " +
            "left join fetch o.client cl " +
            "left join fetch o.guide g " +
            "left join fetch o.tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch o.status s " +
            "where o.id = ?1 and g.username = ?2"
    )
    Optional<Order> findByIdAndGuide_Username(Long id, String guideUsername);

    @Query(
            "select o from Order o " +
            "left join fetch o.client cl " +
            "left join fetch o.guide g " +
            "left join fetch o.tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch o.status s " +
            "where cl.username = ?1"
    )
    List<Order> findAllByClient_Username(String clientUsername, Pageable pageable);

    @Query(
            "select o from Order o " +
            "left join fetch o.client cl " +
            "left join fetch o.guide g " +
            "left join fetch o.tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch o.status s " +
            "where g.username = ?1"
    )
    List<Order> findAllByGuide_Username(String guideUsername, Pageable pageable);
}
