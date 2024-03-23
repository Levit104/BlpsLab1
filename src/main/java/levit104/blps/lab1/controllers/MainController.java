package levit104.blps.lab1.controllers;

import levit104.blps.lab1.dto.UserResponseDTO;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

// TODO обработка исключений
// TODO проверка, что у гида есть роль (при выводе списков экскурсий)

@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    // Информация о пользователе (неважно Гид или нет)
    @GetMapping("/users/{username}")
    public ResponseEntity<?> showUserInfo(@PathVariable String username) {
        User user = userService.getByUsername(username);
        UserResponseDTO responseDTO = modelMapper.map(user, UserResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Выдача роли Гида
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{username}/add-privilege")
    public ResponseEntity<?> addGuidePrivilege(@PathVariable String username,
                                               Principal principal) {

        if (principal.getName().equals(username))
            return ResponseEntity.badRequest().body("Назначение роли администратору"); // TODO исключение/объект ошибки?

        userService.giveGuideRole(username);
        return ResponseEntity.ok("OK");
    }




}
