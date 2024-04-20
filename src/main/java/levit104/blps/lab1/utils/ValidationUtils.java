package levit104.blps.lab1.utils;

import levit104.blps.lab1.exceptions.EntityCreationException;
import levit104.blps.lab1.exceptions.ForbiddenException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationUtils {
    public static final String USERNAME_TAKEN = "Пользователь с указанным именем уже существует";
    public static final String EMAIL_TAKEN = "Пользователь с указанным e-mail уже существует";
    public static final String TOUR_NAME_TAKEN = "Экскурсия с указанным именем уже существует";
    public static final String BLANK_FIELD = "Пустое поле";
    public static final String INVALID_EMAIL = "Некорректный e-mail";
    public static final String INVALID_NUMBER = "Некорректное кол-во людей (от 1 до 25)";
    public static final String PAST_DATE = "Прошедшая дата";

    public static void handleCreationErrors(BindingResult bindingResult) {
        if (!bindingResult.hasErrors())
            return;

        String errorsString = bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(";"));

        throw new EntityCreationException(errorsString);
    }

    public static void checkAccess(String expectedUsername, String actualUsername) {
        if (!expectedUsername.equals(actualUsername))
            throw new ForbiddenException("Нет доступа к данной странице");
    }
}
