package levit104.blps.lab1.dto;

import jakarta.validation.constraints.*;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCreationDTO {
    @NotNull(message = ValidationUtils.BLANK_FIELD)
    @Future(message = ValidationUtils.PAST_DATE)
    private LocalDate tourDate;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    @Min(value = 1, message = ValidationUtils.INVALID_NUMBER)
    @Max(value = 25, message = ValidationUtils.INVALID_NUMBER)
    private Integer numberOfPeople;

    private String description; // может быть null

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private Long tourId;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String guideUsername;
}
