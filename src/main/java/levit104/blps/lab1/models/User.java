package levit104.blps.lab1.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@ToString(exclude = {"tours", "clientOrders", "guideOrders"})
@EqualsAndHashCode(exclude = {"tours", "clientOrders", "guideOrders"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 32)
    private String email;

    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private List<Role> roles;

    // -------------------------------------------
    // TODO - отдельно
    @Column(nullable = false, length = 32)
    private String firstName;

    @Column(nullable = false, length = 32)
    private String lastName;

    @OneToMany(mappedBy = "guide")
    private List<Tour> tours;

    @OneToMany(mappedBy = "client")
    private List<Order> clientOrders;

    @OneToMany(mappedBy = "guide")
    private List<Order> guideOrders;
    // -------------------------------------------
}
