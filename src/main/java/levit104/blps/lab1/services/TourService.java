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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
    private final CityService cityService;
    private final UserService userService;

    public Optional<Tour> findByName(String name) {
        return tourRepository.findByName(name);
    }

    public Tour getByIdAndGuideUsername(Long id, String guideUsername) {
        return tourRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия под номером %d гида '%s' не найдена".formatted(id, guideUsername)
        ));
    }

    public List<Tour> findAllByGuideUsername(String guideUsername) {
        List<Tour> tours = tourRepository.findAllByGuide_Username(guideUsername);

        if (tours.isEmpty())
            throw new EntityNotFoundException("Экскурсии гида '%s' не найдены".formatted(guideUsername));

        return tours;
    }

    public List<Tour> findAllByCityNameAndCountryName(String cityName, String countryName) {
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
