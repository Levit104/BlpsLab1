package levit104.blss.labs.services;

import levit104.blss.labs.exceptions.EntityNotFoundException;
import levit104.blss.labs.models.primary.Role;
import levit104.blss.labs.repos.primary.RoleRepository;
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
