package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.UserDTO;
import levit104.blps.lab1.dto.UserRegistrationDTO;
import levit104.blps.lab1.models.main.User;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final MappingUtils mappingUtils;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registration(@RequestBody @Valid UserRegistrationDTO requestDTO, BindingResult bindingResult) {
        User user = mappingUtils.mapObject(requestDTO, User.class);
        userService.registerUser(user, bindingResult);
        return mappingUtils.mapObject(user, UserDTO.class);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login")
    public String login(CsrfToken csrfToken) {
        return csrfToken.getToken();
    }
}
