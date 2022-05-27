package ro.fasttrackit.budgetapplication.service.product;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.fasttrackit.budgetapplication.model.entity.Product;
import ro.fasttrackit.budgetapplication.exception.EntityNotFoundException;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Product addProduct(Product product) {
        log.info("Adding product {}", product);
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        log.info("Retrieving all products");
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        log.info("Retrieving product {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.forEntity(Product.class, id));
    }

    public Product updateProduct(long id, Product product) {
        log.info("Updating product with id {}", id);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());

                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> EntityNotFoundException.forEntity(Product.class, id));
    }

    public Product deleteProduct(long id) {
        log.info("Deleting product with id {}", id);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    productRepository.delete(existingProduct);
                    return existingProduct;
                })
                .orElseThrow(() -> EntityNotFoundException.forEntity(Product.class, id));
    }
}
