package ro.fasttrackit.budgetapplication.service.role;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.fasttrackit.budgetapplication.model.entity.Role;
import ro.fasttrackit.budgetapplication.exception.EntityNotFoundException;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(Role role) {
        log.info("Creating role {}", role);
        return roleRepository.save(role);
    }

    public List<Role> getRoles() {
        log.info("Getting all roles");
        return roleRepository.findAll();
    }

    public Role getRole(Long id) {
        log.info("Getting role {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forEntity(Role.class, id));
    }

    public Role updateRole(Long id, Role role) {
        log.info("Updating role {}", id);
        return roleRepository.findById(id)
                .map(existingRole -> {
                    existingRole.setName(role.getName());
                    return roleRepository.save(existingRole);
                })
                .orElseThrow(() -> EntityNotFoundException.forEntity(Role.class, id));
    }

    public Role deleteRole(Long id) {
        log.info("Deleting role {}", id);
        return roleRepository
                .findById(id)
                .map(existingRole -> {
                    roleRepository.delete(existingRole);
                    return existingRole;
                }).orElseThrow(() -> EntityNotFoundException.forEntity(Role.class, id));
    }

}
