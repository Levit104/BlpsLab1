package levit104.blps.lab1.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

// TODO проверка client_id != guide_id

@Entity
@Table(name = "orders")
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

    @PrePersist
    public void checkClientGuide() {
        if (client.equals(guide))
            throw new IllegalArgumentException("Клиент и Гид - один и тот же пользователь");
    }
}
