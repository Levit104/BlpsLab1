package levit104.blps.lab1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCreationDTO {
    @NotNull(message = ValidationUtils.BLANK_FIELD)
    private LocalDate tourDate;

    @NotNull(message = ValidationUtils.BLANK_FIELD)
    @Min(value = 1, message = ValidationUtils.INVALID_VALUE)
    private Integer numberOfPeople;

    private String description; // может быть null
}
