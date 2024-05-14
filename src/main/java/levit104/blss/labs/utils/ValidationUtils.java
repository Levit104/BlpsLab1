package levit104.blss.labs.utils;

import levit104.blss.labs.exceptions.EntityCreationException;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationUtils {
    public static final String USERNAME_TAKEN = "Пользователь с указанным именем уже существует";
    public static final String EMAIL_TAKEN = "Пользователь с указанным e-mail уже существует";
    public static final String TOUR_NAME_TAKEN = "Экскурсия с указанным именем уже существует";
    public static final String BLANK_FIELD = "Значение не указано";
    public static final String INVALID_EMAIL = "Некорректный формат e-mail";
    public static final String INVALID_NUMBER_OF_PEOPLE = "Некорректное значение. Должно быть от 1 до 25";
    public static final String PAST_DATE = "Прошедшая дата";
    public static final String INVALID_STRING_SIZE = "Некорректное значение. Должно быть от 2 до 32";

    public static void handleCreationErrors(BindingResult bindingResult) {
        if (!bindingResult.hasErrors())
            return;

        String errorsString = bindingResult.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(";"));

        throw new EntityCreationException(errorsString);
    }
}
