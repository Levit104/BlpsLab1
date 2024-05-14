package levit104.blss.labs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import levit104.blss.labs.utils.ValidationUtils;
import lombok.Data;

@Data
public class TourCreationDTO {
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String name;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private Integer minPrice;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String cityName;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String cityCountryName;
}
