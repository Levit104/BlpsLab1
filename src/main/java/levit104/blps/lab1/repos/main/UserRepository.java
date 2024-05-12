package levit104.blps.lab1.repos.main;

import levit104.blps.lab1.models.main.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            "select u from User u " +
            "left join fetch u.roles " +
            "where u.username = ?1"
    )
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByTours_City_NameAndTours_City_Country_Name(String cityName, String countryName, Pageable pageable);
}
