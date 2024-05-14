package levit104.blss.labs.dto;

import lombok.Data;

@Data
public class TourDTO {
    private Long id;
    private String name;
    private Integer minPrice;
    private String cityName;
    private String cityCountryName;
    private String guideUsername;
}
