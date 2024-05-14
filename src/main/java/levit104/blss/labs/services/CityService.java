package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.models.main.City;
import levit104.blss.labs.repos.main.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public City getByNameAndCountryName(String name, String countryName) {
        return cityRepository.findByNameAndCountry_Name(name, countryName).orElseThrow(() -> new EntityNotFoundException(
                "Город '%s' в стране '%s' не найден".formatted(name, countryName)
        ));
    }
}
