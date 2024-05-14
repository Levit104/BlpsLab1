package levit104.blss.labs.models.primary;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
