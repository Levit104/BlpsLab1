package levit104.blps.lab1.services;

import levit104.blps.lab1.exceptions.EntityNotFoundException;
import levit104.blps.lab1.models.Role;
import levit104.blps.lab1.repos.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Ошибка авторизации"));
    }
}
