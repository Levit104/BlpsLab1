package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.UserRegistrationDTO;
import levit104.blps.lab1.dto.UserResponseDTO;
import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static levit104.blps.lab1.validation.ErrorsUtils.collectErrors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserValidator userValidator;
    private final ModelMapper modelMapper;

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody @Valid UserRegistrationDTO requestDTO,
                                          BindingResult bindingResult) {
        User user = modelMapper.map(requestDTO, User.class);

        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(collectErrors(bindingResult));

        try {
            userService.registerUser(user);
            UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
