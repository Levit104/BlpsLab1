package levit104.blps.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import levit104.blps.lab1.validation.ErrorsUtils;
import lombok.Data;

@Data
public class TourCreationDTO {
    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String name;

    @NotNull(message = ErrorsUtils.BLANK_FIELD)
    private Integer minPrice;

    @NotNull(message = ErrorsUtils.BLANK_FIELD)
    private String cityName;

    @NotNull(message = ErrorsUtils.BLANK_FIELD)
    private String cityCountryName;
}
