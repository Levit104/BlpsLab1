package levit104.blps.lab1.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderResponseDTO {
    private Long id;
    private UserResponseDTO guide;
    private TourResponseDTO tour;
    private LocalDate tourDate;
    private Integer numberOfPeople;
    private String description; // может быть null
    private LocalDate orderDate;
    private String statusName;
}
