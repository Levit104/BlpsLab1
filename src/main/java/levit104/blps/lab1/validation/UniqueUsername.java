package levit104.blps.lab1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import levit104.blps.lab1.utils.ValidationUtils;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default ValidationUtils.USERNAME_TAKEN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
