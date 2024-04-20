package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.UserDTO;
import levit104.blps.lab1.dto.UserRegistrationDTO;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.utils.MappingUtils;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final MappingUtils mappingUtils;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registration(@RequestBody @Valid UserRegistrationDTO requestDTO, BindingResult bindingResult) {
        ValidationUtils.handleCreationErrors(bindingResult);
        User user = mappingUtils.mapObject(requestDTO, User.class);
        userService.registerUser(user);
        return mappingUtils.mapObject(user, UserDTO.class);
    }
}
