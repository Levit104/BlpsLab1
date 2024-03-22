package levit104.blps.lab1.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;
}
