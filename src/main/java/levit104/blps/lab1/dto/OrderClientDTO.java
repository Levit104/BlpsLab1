package levit104.blps.lab1.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderClientDTO {
    private Long id;
    private UserDTO guide;
    private TourDTO tour;
    private LocalDate tourDate;
    private Integer numberOfPeople;
    private String description;
    private LocalDate orderDate;
    private String statusName;
}
