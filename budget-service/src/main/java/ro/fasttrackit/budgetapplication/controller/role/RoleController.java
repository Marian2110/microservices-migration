package ro.fasttrackit.budgetapplication.controller.role;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.budgetapplication.model.dto.RoleDTO;
import ro.fasttrackit.budgetapplication.model.mapper.RoleMapper;
import ro.fasttrackit.budgetapplication.service.role.RoleService;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@AllArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleMapper.mapToDTOs(roleService.getRoles());
    }

    @GetMapping("/{id}")
    public RoleDTO getRoleById(@PathVariable("id") Long id) {
        return roleMapper.mapToDTO(roleService.getRole(id));
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return roleMapper.mapToDTO(roleService.createRole(roleMapper.mapToEntity(roleDTO)));
    }

    @PutMapping("/{id}")
    public RoleDTO updateRole(@PathVariable("id") Long id, @RequestBody RoleDTO roleDTO) {
        return roleMapper.mapToDTO(roleService.updateRole(id, roleMapper.mapToEntity(roleDTO)));
    }

    @DeleteMapping("/{id}")
    public RoleDTO deleteRole(@PathVariable("id") Long id) {
        return roleMapper.mapToDTO(roleService.deleteRole(id));
    }

}