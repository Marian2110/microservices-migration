package ro.fasttrackit.budgetapplication.service.role;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.budgetapplication.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
