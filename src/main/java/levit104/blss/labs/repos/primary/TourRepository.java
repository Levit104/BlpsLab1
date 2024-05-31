package levit104.blss.labs.repos.primary;

import levit104.blss.labs.models.primary.Tour;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    boolean existsByName(String name);

    @Query(
            "select t from Tour t " +
                    "left join fetch t.city ci " +
                    "left join fetch ci.country co " +
                    "left join fetch t.guide g " +
                    "where t.id = ?1 and g.username = ?2 and t.approved"
    )
    Optional<Tour> findByIdAndGuide_UsernameAndApprovedIsTrue(Long id, String guideUsername);

    @Query(
            "select t from Tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch t.guide g " +
            "where t.id = ?1 and g.username = ?2"
    )
    Optional<Tour> findByIdAndGuide_Username(Long id, String guideUsername);

    @Query(
            "select t from Tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch t.guide g " +
            "where g.username = ?1 and t.approved"
    )
    List<Tour> findAllByGuide_UsernameAndApprovedIsTrue(String guideUsername, Pageable pageable);

    @Query(
            "select t from Tour t " +
            "left join fetch t.city ci " +
            "left join fetch ci.country co " +
            "left join fetch t.guide g " +
            "where ci.name = ?1 and co.name = ?2 and t.approved"
    )
    List<Tour> findAllByCity_NameAndCity_Country_NameAndApprovedIsTrue(String cityName, String countryName, Pageable pageable);
}
