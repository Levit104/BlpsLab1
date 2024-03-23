package levit104.blps.lab1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderCreationDTO {
    @NotNull(message = "Заполните поле")
    private LocalDate tourDate; // TODO валидация даты

    @NotNull(message = "Заполните поле")
    private Integer numberOfPeople; // TODO валидация числа

    private String description; // может быть null
}
