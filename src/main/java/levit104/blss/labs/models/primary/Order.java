package levit104.blss.labs.models.primary;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Check;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Check(constraints = "number_of_people >= 1 and number_of_people <= 25")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id", nullable = false)
    private User guide;

    @ManyToOne
    @JoinColumn(name = "tour_id", referencedColumnName = "id", nullable = false)
    private Tour tour;

    @Column(nullable = false)
    private LocalDate tourDate;

    @Column(nullable = false)
    private Integer numberOfPeople;

    private String description; // может быть null

    @Column(nullable = false)
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private OrderStatus status;
}
