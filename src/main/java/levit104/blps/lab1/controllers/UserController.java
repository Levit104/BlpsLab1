package levit104.blps.lab1.controllers;

import levit104.blps.lab1.dto.UserDTO;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import levit104.blps.lab1.utils.MappingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MappingUtils mappingUtils;

    // Информация о пользователе (любом)
    @GetMapping("/users/{username}")
    public UserDTO showUserInfo(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return mappingUtils.mapObject(user, UserDTO.class);
    }

    // Выдача роли Гида
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/users/{username}/add-privilege")
    public String addGuidePrivilege(@PathVariable String username,
                                    Principal principal) {
        return userService.giveGuideRole(principal.getName(), username);
    }
}
