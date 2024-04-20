package levit104.blps.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import levit104.blps.lab1.utils.ValidationUtils;
import levit104.blps.lab1.validation.UniqueTourName;
import lombok.Data;

@Data
public class TourCreationDTO {
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @UniqueTourName
    private String name;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private Integer minPrice;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String cityName;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String cityCountryName;
}
