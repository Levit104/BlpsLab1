package levit104.blps.lab1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @Email(message = "Некорректное значение")
    @NotBlank(message = "Заполните поле")
    private String email;

    @NotBlank(message = "Заполните поле")
    private String username;

    @NotBlank(message = "Заполните поле")
    private String password;

    @NotBlank(message = "Заполните поле")
    private String firstName;

    @NotBlank(message = "Заполните поле")
    private String lastName;
}
