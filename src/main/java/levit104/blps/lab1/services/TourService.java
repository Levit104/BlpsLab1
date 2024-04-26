package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.City;
import levit104.blps.lab1.models.Tour;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.repos.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
    private final CityService cityService;
    private final UserService userService;

    public Tour getByIdAndGuideUsername(Long id, String guideUsername) {
        return tourRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия №%d у '%s' не найдена".formatted(id, guideUsername)
        ));
    }

    public List<Tour> getAllByGuideUsername(String guideUsername) {
        List<Tour> tours = tourRepository.findAllByGuide_Username(guideUsername);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии у '%s' не найдены".formatted(guideUsername));

        return tours;
    }

    public List<Tour> getAllByCityNameAndCountryName(String cityName, String countryName) {
        List<Tour> tours = tourRepository.findAllByCity_NameAndCity_Country_Name(cityName, countryName);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии в городе '%s' в стране '%s' не найдены"
                    .formatted(cityName, countryName));

        return tours;
    }

    @Transactional
    public void add(Tour tour, String guideUsername) {
        City city = cityService.getByNameAndCountryName(tour.getCity().getName(), tour.getCity().getCountry().getName());
        User guide = userService.getByUsername(guideUsername);
        tour.setCity(city);
        tour.setGuide(guide);
        tourRepository.save(tour);
    }
}
