package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.exceptions.InvalidDataException;
import levit104.blps.lab1.models.main.Role;
import levit104.blps.lab1.models.main.User;
import levit104.blps.lab1.repos.main.UserRepository;
import levit104.blps.lab1.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                "Пользователь '%s' не найден".formatted(username)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList()
        );
    }

    // Чтобы выводить сообщение о том, что пользователь не найден (сообщение у UsernameNotFoundException игнорируется)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(
                "Пользователь '%s' не найден".formatted(username)
        ));
    }

    public List<User> getAllByCityNameAndCountryName(String cityName, String countryName) {
        List<User> guides = userRepository.findAllByTours_City_NameAndTours_City_Country_Name(cityName, countryName);

        if (guides.isEmpty())
            throw new EntityNotFoundException("Гиды в городе '%s' в стране '%s' не найдены"
                    .formatted(cityName, countryName));

        return guides;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void registerUser(User user, BindingResult bindingResult) {
        if (userRepository.existsByEmail(user.getEmail()))
            bindingResult.rejectValue("email", "", ValidationUtils.EMAIL_TAKEN);
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            bindingResult.rejectValue("username", "", ValidationUtils.USERNAME_TAKEN);
        ValidationUtils.handleCreationErrors(bindingResult);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleService.getByName("ROLE_USER")));
        userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public String giveGuideRole(String username) {
        String roleName = "ROLE_GUIDE";
        User user = getByUsername(username);
        Role role = roleService.getByName(roleName);

        if (user.getRoles().contains(role))
            throw new InvalidDataException("Роль '%s' уже была выдана".formatted(roleName));

        user.getRoles().add(role);
        userRepository.save(user);

        return "Роль '%s' успешно выдана".formatted(roleName);
    }
}
