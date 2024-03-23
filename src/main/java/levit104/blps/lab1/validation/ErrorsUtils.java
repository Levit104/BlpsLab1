package levit104.blps.lab1.validation;

import levit104.blps.lab1.dto.UserErrorsDTO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorsUtils {
    public static final String USERNAME_TAKEN = "Пользователь с указанным именем уже существует";
    public static final String EMAIL_TAKEN = "Пользователь с указанным email уже существует";
    public static final String TOUR_NAME_TAKEN = "Экскурсия с указанным именем уже существует";

    public static List<UserErrorsDTO> returnErrors(BindingResult bindingResult) {
        Map<String, UserErrorsDTO> errorsMap = new HashMap<>();

        for (FieldError error : bindingResult.getFieldErrors()) {
            UserErrorsDTO userError = errorsMap.get(error.getField());

            if (userError == null) {
                userError = new UserErrorsDTO(error.getField(), new ArrayList<>(), error.getRejectedValue());
                errorsMap.put(error.getField(), userError);
            }

            userError.getErrorsMessages().add(error.getDefaultMessage());
        }
        /*
        bindingResult.getFieldErrors().forEach(error -> errorsMap
                .computeIfAbsent(error.getField(), field -> new UserErrorsDTO(field, new ArrayList<>(), error.getRejectedValue()))
                .getFieldErrorsMessages().add(error.getDefaultMessage()));
        return new ArrayList<>(errorsMap.values());
        */

        return new ArrayList<>(errorsMap.values());
    }
}
