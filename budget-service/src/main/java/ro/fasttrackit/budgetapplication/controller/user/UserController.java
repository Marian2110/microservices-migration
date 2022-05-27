package ro.fasttrackit.budgetapplication.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.budgetapplication.model.dto.RoleDTO;
import ro.fasttrackit.budgetapplication.model.dto.UserDTO;
import ro.fasttrackit.budgetapplication.model.entity.User;
import ro.fasttrackit.budgetapplication.model.mapper.RoleMapper;
import ro.fasttrackit.budgetapplication.model.mapper.UserMapper;
import ro.fasttrackit.budgetapplication.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    private final RoleMapper roleMapper;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userMapper.mapToDTOs(userService.getUsers());
    }

    @GetMapping(path = "/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userMapper.mapToDTO(userService.getUser(id));
    }

    @PostMapping
    public UserDTO addUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.mapToEntity(userDTO);
        return userMapper.mapToDTO(userService.addUser(user));
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user = userMapper.mapToEntity(userDTO);
        return userMapper.mapToDTO(userService.updateUser(id, user));
    }

    @DeleteMapping(path = "/{id}")
    public UserDTO deleteUser(@PathVariable Long id) {
        return userMapper.mapToDTO(userService.deleteUser(id));
    }

    // Work in progress
    @GetMapping(path = "/{id}/roles")
    public List<RoleDTO> getRoles(@PathVariable Long id) {
        return roleMapper.mapToDTOs(userService.getRoles(id));
    }

    @PostMapping(path = "/{id}/roles")
    public UserDTO addRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return userMapper.mapToDTO(userService.addRole(id, roleDTO.getId()));
    }

    @DeleteMapping(path = "/{id}/roles/{roleId}")
    public UserDTO deleteRole(@PathVariable Long id, @PathVariable Long roleId) {
        return userMapper.mapToDTO(userService.removeRole(id, roleId));
    }
}
