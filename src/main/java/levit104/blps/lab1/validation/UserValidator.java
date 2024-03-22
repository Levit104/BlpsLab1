package levit104.blps.lab1.validation;

import levit104.blps.lab1.models.User;
import levit104.blps.lab1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;

        if (userService.findByUsername(user.getUsername()).isPresent())
            errors.rejectValue("username", "", "Пользователь с указанным именем уже существует");

        if (userService.findByEmail(user.getEmail()).isPresent())
            errors.rejectValue("email", "", "Пользователь с указанным email уже существует");
    }
}
