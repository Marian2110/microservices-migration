package ro.fasttrackit.budgetapplication.controller.product;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.budgetapplication.model.dto.ProductDTO;
import ro.fasttrackit.budgetapplication.service.product.ProductService;
import ro.fasttrackit.budgetapplication.model.mapper.ProductMapper;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@AllArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;

    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productMapper.mapToDTOs(productService.getAllProducts());
    }

    @GetMapping(path = "{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productMapper.mapToDTO(productService.getProduct(id));
    }

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO productDTO) {
        return productMapper.mapToDTO(
                productService.addProduct(
                        productMapper.mapToEntity(productDTO)));
    }

    @PutMapping(path = "{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productMapper.mapToDTO(
                productService.updateProduct(
                        id,
                        productMapper.mapToEntity(productDTO)));
    }

    @DeleteMapping(path = "{id}")
    public ProductDTO deleteProduct(@PathVariable Long id) {
        return productMapper.mapToDTO(
                productService.deleteProduct(id));
    }
}
