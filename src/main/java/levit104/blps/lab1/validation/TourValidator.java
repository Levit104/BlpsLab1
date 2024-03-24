package levit104.blps.lab1.validation;

import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.services.TourService;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TourValidator implements Validator {
    private final TourService tourService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Tour.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Tour tour = (Tour) target;

        if (tourService.findByName(tour.getName()).isPresent())
            errors.rejectValue("name", "", ValidationUtils.TOUR_NAME_TAKEN);
    }
}
