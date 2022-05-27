package ro.fasttrackit.budgetapplication.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ro.fasttrackit.budgetapplication.model.dto.ProductDTO;
import ro.fasttrackit.budgetapplication.model.entity.Product;

import java.util.List;
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO mapToDTO(Product product);
    Product mapToEntity(ProductDTO productDto);

    List<ProductDTO> mapToDTOs(List<Product> products);
}
