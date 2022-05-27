package ro.fasttrackit.budgetapplication.service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttrackit.budgetapplication.model.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
