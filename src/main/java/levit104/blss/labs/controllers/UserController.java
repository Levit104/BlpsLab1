package levit104.blss.labs.controllers;

import levit104.blss.labs.dto.UserDTO;
import levit104.blss.labs.models.primary.User;
import levit104.blss.labs.services.UserService;
import levit104.blss.labs.utils.MappingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MappingHelper mappingHelper;

    // Информация о пользователе (любом)
    @GetMapping("/users/{username}")
    public UserDTO showUserInfo(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return mappingHelper.mapObject(user, UserDTO.class);
    }

    // Выдача роли Гида
    @PreAuthorize("hasRole('ADMIN') && principal.username != #username")
    @PatchMapping("/users/{username}/add-privilege")
    public String addGuidePrivilege(@PathVariable String username) {
        return userService.giveGuideRole(username);
    }
}
