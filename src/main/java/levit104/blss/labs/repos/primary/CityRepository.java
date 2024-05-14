package levit104.blss.labs.repos.primary;

import levit104.blss.labs.models.primary.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @Query(
            "select ci from City ci " +
            "left join fetch ci.country co " +
            "where ci.name = ?1 and co.name = ?2"
    )
    Optional<City> findByNameAndCountry_Name(String name, String countryName);
}
