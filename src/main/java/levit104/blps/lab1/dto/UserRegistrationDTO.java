package levit104.blps.lab1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @Email(message = ValidationUtils.INVALID_VALUE)
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String email;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String username;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String password;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String firstName;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String lastName;
}
