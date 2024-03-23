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

    public Optional<Tour> findByName(String name) {
        return tourRepository.findByName(name);
    }

    public Tour getByIdAndGuideUsername(Long id, String guideUsername) throws EntityNotFoundException {
        return tourRepository.findByIdAndGuide_Username(id, guideUsername).orElseThrow(() -> new EntityNotFoundException(
                "Экскурсия под номером %d пользователя %s не найдена".formatted(id, guideUsername)
        ));
    }

    public List<Tour> findAllByGuideUsername(String guideUsername) {
        return tourRepository.findAllByGuide_Username(guideUsername);
    }

    public List<Tour> findAllByCityNameAndCountryName(String cityName, String countryName) {
        return tourRepository.findAllByCity_NameAndCity_Country_Name(cityName, countryName);
    }

    @Transactional
    public void add(Tour tour, User guide) throws EntityNotFoundException {
        City city = cityService.getByNameAndCountryName(
                tour.getCity().getName(), tour.getCity().getCountry().getName()
        );
        tour.setCity(city);
        tour.setGuide(guide);
        tourRepository.save(tour);
    }
}
