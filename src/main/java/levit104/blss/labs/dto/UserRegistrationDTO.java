package levit104.blss.labs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import levit104.blss.labs.utils.ValidationUtils;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @Email(message = ValidationUtils.INVALID_EMAIL)
    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String email;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String username;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    private String password;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String firstName;

    @NotBlank(message = ValidationUtils.BLANK_FIELD)
    @Size(min = 2, max = 32, message = ValidationUtils.INVALID_STRING_SIZE)
    private String lastName;
}
