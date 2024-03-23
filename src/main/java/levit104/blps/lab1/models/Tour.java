package levit104.blps.lab1.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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
}
