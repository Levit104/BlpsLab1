package levit104.blps.lab1.validation;

import levit104.blps.lab1.models.Order;
import levit104.blps.lab1.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OrderValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Order order = (Order) target;

        if (order.getTourDate().isBefore(LocalDate.now()))
            errors.rejectValue("tourDate", "", ErrorsUtils.INVALID_VALUE);
    }
}
