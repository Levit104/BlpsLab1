package levit104.blps.lab1.dto;

import lombok.Data;

@Data
public class TourResponseDTO {
    private Long id;
    private String name;
    private Integer minPrice;
    private String cityName;
    private String cityCountryName;
    private String guideUsername;
}
