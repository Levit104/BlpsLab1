package levit104.blss.labs.models.main;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"cities"})
@EqualsAndHashCode(exclude = {"cities"})
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    private String name;

    @OneToMany(mappedBy = "country")
    private List<City> cities;
}