package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.exceptions.InvalidDataException;
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

// TODO проверка, что у гида есть роль (при выводе списков экскурсий)
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Чтобы выводить сообщение о том, что пользователь не найден (сообщение у UsernameNotFoundException игнорируется)
    public User getByUsername(String username) {
        return findByUsername(username).orElseThrow(() -> new EntityNotFoundException(
                "Пользователь '%s' не найден".formatted(username)
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь '%s' не найден".formatted(username)
        ));
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
    public String giveGuideRole(String adminName, String username) {
        if (adminName.equals(username))
            throw new InvalidDataException("Выдача роли администратору");

        User user = getByUsername(username);
        Role role = roleService.getByName("ROLE_GUIDE");

        if (user.getRoles().contains(role))
            throw new InvalidDataException("Роль уже была выдана");

        user.getRoles().add(role);
        userRepository.save(user);

        return "Роль успешно выдана";
    }

    public List<User> findAllByCityNameAndCountryName(String cityName, String countryName) {
        List<User> guides = userRepository.findAllByTours_City_NameAndTours_City_Country_Name(cityName, countryName);

        if (guides.isEmpty())
            throw new EntityNotFoundException("Гиды в городе '%s' в стране '%s' не найдены"
                    .formatted(cityName, countryName));

        return guides;
    }
}
