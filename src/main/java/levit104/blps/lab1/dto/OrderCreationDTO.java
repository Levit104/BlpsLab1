package levit104.blps.lab1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import levit104.blps.lab1.validation.ErrorsUtils;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCreationDTO {
    @NotNull(message = ErrorsUtils.BLANK_FIELD)
    private LocalDate tourDate;

    @NotNull(message = ErrorsUtils.BLANK_FIELD)
    @Min(value = 1, message = ErrorsUtils.INVALID_VALUE)
    private Integer numberOfPeople;

    private String description; // может быть null
}
