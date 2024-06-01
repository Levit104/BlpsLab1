package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.exceptions.InvalidDataException;
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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
    private final CityService cityService;
    private final UserService userService;
    private final NotificationProducerService producer;

    public Tour getByIdAndGuideUsername(Long id, String guideUsername) {
        return tourRepository.findByIdAndGuide_UsernameAndApprovedIsTrue(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия №%d у '%s' не найдена".formatted(id, guideUsername)
        ));
    }

    public List<Tour> getAllByGuideUsername(String guideUsername, Pageable pageable) {
        List<Tour> tours = tourRepository.findAllByGuide_UsernameAndApprovedIsTrue(guideUsername, pageable);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии у '%s' не найдены".formatted(guideUsername));

        return tours;
    }

    public List<Tour> getAllByCityNameAndCountryName(String cityName, String countryName, Pageable pageable) {
        List<Tour> tours = tourRepository.findAllByCity_NameAndCity_Country_NameAndApprovedIsTrue(cityName, countryName, pageable);

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
        tour.setCreationDate(LocalDate.now());
        tourRepository.save(tour);

        String message = "Экскурсия %d создана".formatted(tour.getId());
        List<String> usernames = userService.getAllByRoleName("ROLE_ADMIN").stream().map(User::getUsername).toList();
        producer.send(message, usernames);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String changeApprovalStatus(Long id, String guideUsername, boolean approved) {
        Tour tour = tourRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия №%d у '%s' не найдена".formatted(id, guideUsername)
        ));

        if (tour.getApproved() != null) {
            throw new InvalidDataException("Экскурсия уже была рассмотрена");
        }

        tour.setApproved(approved);

        String message = approved ? "Экскурсия %d одобрена".formatted(id) : "Экскурсия %d отклонена".formatted(id);
        List<String> usernames = List.of(guideUsername);
        producer.send(message, usernames);
        return message;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Tour> deleteAllDisapprovedWithCreationDateBefore(LocalDate date) {
        List<Tour> tours = tourRepository.findAllByApprovedFalseAndCreationDateBefore(date);
        tourRepository.deleteAll(tours);
        return tours;
    }

    private void validateTour(Tour tour, BindingResult bindingResult) {
        if (tourRepository.existsByName(tour.getName()))
            bindingResult.rejectValue("name", "", ValidationUtils.TOUR_NAME_TAKEN);
        ValidationUtils.handleCreationErrors(bindingResult);
    }
}
