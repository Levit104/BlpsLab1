package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.main.Role;
import levit104.blps.lab1.repos.main.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException(
                "Роль '%s' не найдена".formatted(name)
        ));
    }
}
