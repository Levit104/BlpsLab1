package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.City;
import levit104.blps.lab1.repos.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<City> findAllByCountryName(String countryName) {
        return cityRepository.findAllByCountry_Name(countryName);
    }

    public City getByNameAndCountryName(String name, String countryName) {
        return findByNameAndCountryName(name, countryName).orElseThrow(() -> new EntityNotFoundException(
                "Город %s в стране %s не найден".formatted(name, countryName)
        ));
    }

    public Optional<City> findByNameAndCountryName(String name, String countryName) {
        return cityRepository.findByNameAndCountry_Name(name, countryName);
    }
}
