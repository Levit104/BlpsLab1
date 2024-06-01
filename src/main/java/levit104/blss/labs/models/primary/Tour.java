package levit104.blss.labs.models.primary;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"orders"})
@EqualsAndHashCode(exclude = {"orders"})
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    private String name;

    @Column(nullable = false)
    private Integer minPrice;

    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "id", nullable = false)
    private User guide;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "tour")
    private List<Order> orders;

    private Boolean approved;

    @Column(nullable = false)
    private LocalDate creationDate;
}
