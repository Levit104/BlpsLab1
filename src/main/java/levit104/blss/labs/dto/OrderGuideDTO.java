package levit104.blss.labs.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderGuideDTO {
    private Long id;
    private UserDTO client;
    private TourDTO tour;
    private LocalDate tourDate;
    private Integer numberOfPeople;
    private String description;
    private LocalDate orderDate;
    private String statusName;
}
