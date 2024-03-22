package levit104.blps.lab1.controllers;

import jakarta.validation.Valid;
import levit104.blps.lab1.dto.UserRegistrationDTO;
import levit104.blps.lab1.dto.UserResponseDTO;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static levit104.blps.lab1.validation.ErrorsUtils.returnErrors;

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
            return ResponseEntity.badRequest().body(returnErrors(bindingResult));

        userService.registerUser(user);
        UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }


}
