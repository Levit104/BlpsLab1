package levit104.blps.lab1.repos;

import levit104.blps.lab1.models.Tour;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    Optional<Tour> findByName(String name);

    Optional<Tour> findByIdAndGuide_Username(Long id, String guideUsername);

    List<Tour> findAllByGuide_Username(String guideUsername);

    List<Tour> findAllByCity_NameAndCity_Country_Name(String cityName, String countryName);
}
