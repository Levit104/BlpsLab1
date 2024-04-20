package levit104.blps.lab1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import levit104.blps.lab1.repos.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueTourNameValidator implements ConstraintValidator<UniqueTourName, String> {
    private final TourRepository tourRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return tourRepository.findByName(value).isEmpty();
    }
}
