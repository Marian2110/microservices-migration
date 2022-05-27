package ro.fasttrackit.budgetapplication.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ro.fasttrackit.budgetapplication.model.dto.TransactionDTO;
import ro.fasttrackit.budgetapplication.model.entity.Transaction;

import java.util.List;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "user.id", target = "userId")
    TransactionDTO mapToDTO(Transaction transaction);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "userId", target = "user.id")
    Transaction mapToEntity(TransactionDTO transactionDTO);

    List<TransactionDTO> mapToDTOs(List<Transaction> transactions);

    default Page<TransactionDTO> toDtoPage(Page<Transaction> transactions) {
        return new PageImpl<>(mapToDTOs(transactions.getContent()), transactions.getPageable(), transactions.getTotalElements());
    }

}
