package levit104.blss.labs.dto;

import jakarta.validation.constraints.*;
import levit104.blss.labs.utils.ValidationUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCreationDTO {
    @NotNull(message = ValidationUtils.BLANK_FIELD)
    @Future(message = ValidationUtils.PAST_DATE)
    private LocalDate tourDate;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    @Min(value = 1, message = ValidationUtils.INVALID_NUMBER_OF_PEOPLE)
    @Max(value = 25, message = ValidationUtils.INVALID_NUMBER_OF_PEOPLE)
    private Integer numberOfPeople;

    private String description; // может быть null

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private Long tourId;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String guideUsername;
}
