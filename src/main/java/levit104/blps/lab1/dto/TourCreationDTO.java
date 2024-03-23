package levit104.blps.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TourCreationDTO {
    @NotBlank(message = "Заполните поле")
    private String name;

    @NotNull(message = "Заполните поле")
    private Integer minPrice;

    @NotNull(message = "Заполните поле")
    private String cityName;

    @NotNull(message = "Заполните поле")
    private String cityCountryName;
}
