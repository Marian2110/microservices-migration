package ro.fasttrackit.budgetapplication.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.budgetapplication.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
