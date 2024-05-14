package levit104.blss.labs.models.primary;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"orders"})
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String name;

    @OneToMany(mappedBy = "status")
    private List<Order> orders;
}
