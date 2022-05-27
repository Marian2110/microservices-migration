package ro.fasttrackit.budgetapplication.controller.transaction;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.fasttrackit.budgetapplication.model.dto.TransactionDTO;
import ro.fasttrackit.budgetapplication.model.mapper.TransactionMapper;
import ro.fasttrackit.budgetapplication.service.transaction.TransactionService;
import ro.fasttrackit.budgetapplication.utils.Criteria;
import ro.fasttrackit.budgetapplication.utils.TransactionInfo;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "${api.v1.base-path}/transactions")
@AllArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping(path = "{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionMapper.mapToDTO(transactionService.findById(id));
    }

    @PostMapping
    public TransactionDTO addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        return transactionMapper.mapToDTO(
                transactionService.add(
                        transactionMapper.mapToEntity(transactionDTO)));
    }

    @PutMapping(path = "{id}")
    public TransactionDTO updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) {
        return transactionMapper.mapToDTO(
                transactionService.update(
                        id,
                        transactionMapper.mapToEntity(transactionDTO)));
    }

    @DeleteMapping(path = "{id}")
    public TransactionDTO deleteTransaction(@PathVariable Long id) {
        return transactionMapper.mapToDTO(transactionService.delete(id));
    }

    @GetMapping("/reports/type")
    public Map<String, List<TransactionInfo>> getTransactionsGroupedByType() {
        return transactionService.getTransactionsGroupedByType();
    }

    @GetMapping("/reports/product")
    public Map<String, List<TransactionInfo>> getTransactionsGroupedByProduct() {
        return transactionService.getTransactionsGroupedByProduct();
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
    public Page<TransactionDTO> find(@Valid @RequestBody Criteria criteria) {
        return transactionMapper.toDtoPage(transactionService.findUsingDao(criteria));
    }

}

