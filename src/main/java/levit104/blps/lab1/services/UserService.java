package levit104.blps.lab1.services;

import levit104.blps.lab1.models.Role;
import levit104.blps.lab1.models.User;
import levit104.blps.lab1.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User getByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь %s не найден".formatted(username))
        );
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    @Transactional
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleService.getByName("ROLE_USER")));
        userRepository.save(user);
    }

    @Transactional
    public void giveGuideRole(String username) {
        User user = getByUsername(username);

        Role role = roleService.getByName("ROLE_GUIDE");

        if (user.getRoles().contains(role))
            return;

        user.getRoles().add(role);

        userRepository.save(user);
        System.out.println(user.getRoles());
    }

    public List<User> findAllByCityNameAndCountryName(String cityName, String countryName) {
        return userRepository.findAllByTours_City_NameAndTours_City_Country_Name(cityName, countryName);
    }
}
