package levit104.blss.labs.controllers;

import jakarta.validation.Valid;
import levit104.blss.labs.dto.UserDTO;
import levit104.blss.labs.dto.UserRegistrationDTO;
import levit104.blss.labs.models.main.User;
import levit104.blss.labs.services.UserService;
import levit104.blss.labs.utils.MappingHelper;
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
    private final MappingHelper mappingHelper;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registration(@RequestBody @Valid UserRegistrationDTO requestDTO, BindingResult bindingResult) {
        User user = mappingHelper.mapObject(requestDTO, User.class);
        userService.registerUser(user, bindingResult);
        return mappingHelper.mapObject(user, UserDTO.class);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/login")
    public String login(CsrfToken csrfToken) {
        return csrfToken.getToken();
    }
}
