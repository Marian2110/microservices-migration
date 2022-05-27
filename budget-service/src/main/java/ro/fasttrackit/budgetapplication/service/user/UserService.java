package ro.fasttrackit.budgetapplication.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.fasttrackit.budgetapplication.exception.EntityNotFoundException;
import ro.fasttrackit.budgetapplication.model.entity.Role;
import ro.fasttrackit.budgetapplication.model.entity.User;
import ro.fasttrackit.budgetapplication.service.role.RoleService;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private RoleService roleService;

    public User addUser(User user) {
        log.info("Creating user {}", user);
        user.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        log.info("Getting user {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forEntity(User.class, id));
    }

    public User updateUser(Long id, User user) {
        log.info("Updating user {}", id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));
                    existingUser.setRoles(user.getRoles());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> EntityNotFoundException.forEntity(User.class, id));
    }

    public User deleteUser(Long id) {
        log.info("Deleting user {}", id);
        return userRepository
                .findById(id)
                .map(existingUser -> {
                    userRepository.delete(existingUser);
                    return existingUser;
                }).orElseThrow(() -> EntityNotFoundException.forEntity(User.class, id));
    }

    public User addRole(Long id, Long roleId) {
        log.info("Adding role {} to user {}", roleId, id);
        User user = getUser(id);
        Role role = roleService.getRole(roleId);
        List<Role> roles = getRoles(id);
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public List<Role> getRoles(Long id) {
        return userRepository.findById(id)
                .map(User::getRoles)
                .orElseThrow(() -> EntityNotFoundException.forEntity(User.class, id));
    }

    public User removeRole(Long id, Long roleId) {
        log.info("Removing role {} from user {}", roleId, id);
        User user = getUser(id);
        Role role = roleService.getRole(roleId);
        List<Role> roles = getRoles(id);
        roles.remove(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

}
