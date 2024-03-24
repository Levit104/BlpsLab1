package levit104.blps.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.Data;

@Data
public class TourCreationDTO {
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String name;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private Integer minPrice;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private String cityName;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private String cityCountryName;
}
