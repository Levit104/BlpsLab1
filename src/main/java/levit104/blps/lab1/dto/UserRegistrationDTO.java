package levit104.blps.lab1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import levit104.blps.lab1.validation.ErrorsUtils;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @Email(message = ErrorsUtils.INVALID_VALUE)
    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String email;

    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String username;

    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String password;

    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String firstName;

    @NotBlank(message = ErrorsUtils.BLANK_FIELD)
    private String lastName;
}
