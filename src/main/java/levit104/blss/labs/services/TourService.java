package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.models.primary.City;
import levit104.blss.labs.models.primary.Tour;
import levit104.blss.labs.models.primary.User;
import levit104.blss.labs.repos.primary.TourRepository;
import levit104.blss.labs.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
    private final CityService cityService;
    private final UserService userService;
    private final NotificationService notificationService;

    public Tour getByIdAndGuideUsername(Long id, String guideUsername) {
        return tourRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия №%d у '%s' не найдена".formatted(id, guideUsername)
        ));
    }

    public List<Tour> getAllByGuideUsername(String guideUsername, Pageable pageable) {
        List<Tour> tours = tourRepository.findAllByGuide_Username(guideUsername, pageable);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии у '%s' не найдены".formatted(guideUsername));

        return tours;
    }

    public List<Tour> getAllByCityNameAndCountryName(String cityName, String countryName, Pageable pageable) {
        List<Tour> tours = tourRepository.findAllByCity_NameAndCity_Country_Name(cityName, countryName, pageable);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии в городе '%s' в стране '%s' не найдены"
                    .formatted(cityName, countryName));

        return tours;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void add(Tour tour, String guideUsername, BindingResult bindingResult) {
        validateTour(tour, bindingResult);

        City city = cityService.getByNameAndCountryName(tour.getCity().getName(), tour.getCity().getCountry().getName());
        User guide = userService.getByUsername(guideUsername);
        tour.setCity(city);
        tour.setGuide(guide);
        tourRepository.save(tour);

        notificationService.createNotification("Экскурсия %d создана".formatted(tour.getId()), guideUsername);
    }

    private void validateTour(Tour tour, BindingResult bindingResult) {
        if (tourRepository.existsByName(tour.getName()))
            bindingResult.rejectValue("name", "", ValidationUtils.TOUR_NAME_TAKEN);
        ValidationUtils.handleCreationErrors(bindingResult);
    }
}
