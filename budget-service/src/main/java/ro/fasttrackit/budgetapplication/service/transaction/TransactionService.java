package ro.fasttrackit.budgetapplication.service.transaction;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ro.fasttrackit.budgetapplication.exception.EntityNotFoundException;
import ro.fasttrackit.budgetapplication.model.entity.Transaction;
import ro.fasttrackit.budgetapplication.utils.Criteria;
import ro.fasttrackit.budgetapplication.utils.TransactionInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    private final TransactionDao transactionDao;

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id) {
        return transactionRepository
                .findById(id)
                .orElseThrow(() -> EntityNotFoundException.forEntity(Transaction.class, id));
    }

    public Transaction add(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, Transaction transaction) {
        return transactionRepository
                .findById(id)
                .map(existingTransaction -> {
                    existingTransaction.setProduct(transaction.getProduct());
                    existingTransaction.setType(transaction.getType());
                    existingTransaction.setAmount(transaction.getAmount());
                    return transactionRepository.save(existingTransaction);
                }).orElseThrow(() -> EntityNotFoundException.forEntity(Transaction.class, id));
    }

    public Transaction delete(Long id) {
        return transactionRepository
                .findById(id)
                .map(existingTransaction -> {
                    transactionRepository.delete(existingTransaction);
                    return existingTransaction;
                }).orElseThrow(() -> EntityNotFoundException.forEntity(Transaction.class, id));
    }

    public void generateReport(String filePath) {
        log.info("Generating report for all users");

        Month month = LocalDateTime.now().getMonth() == Month.JANUARY ? Month.DECEMBER : LocalDateTime.now().getMonth().minus(1);
        Year year = LocalDateTime.now().getMonth() == Month.JANUARY ? Year.of(LocalDateTime.now().getYear() - 1) : Year.of(LocalDateTime.now().getYear());

        LocalDateTime startDate = LocalDateTime.of(year.getValue(), month, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 30, 0, 0);

        String dateString = startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String fileName = filePath + "-" + dateString + ".txt";

        Map<String, List<TransactionInfo>> report = mapTransactionsToUsers(startDate, endDate);
        writeFile(filePath, report);
    }

    private void writeFile(String filePath, Map<String, List<TransactionInfo>> report) {
        StringBuilder sb = new StringBuilder();
        report.forEach((username, transactions) -> {
            sb.append("User: ").append(username).append("\n");
            transactions.forEach(transactionInfo -> sb.append("\tType: ")
                    .append(transactionInfo.getType()).append(" Amount: ")
                    .append(transactionInfo.getAmount()).append("\n"));
            sb.append("\n");
        });

        try {
            Files.write(Paths.get(filePath), sb.toString().getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<TransactionInfo>> mapTransactionsToUsers(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, List<TransactionInfo>> report = new HashMap<>();
        transactionRepository.findByConfirmedTrueAndCreatedAtBetween(startDate, endDate)
                .forEach(transaction -> {
                    TransactionInfo transactionInfo = TransactionInfo.builder()
                            .type(transaction.getType())
                            .amount(transaction.getAmount())
                            .build();
                    if (report.containsKey(transaction.getUser().getUsername())) {
                        report.get(transaction.getUser().getUsername()).add(
                                transactionInfo);
                    } else {
                        List<TransactionInfo> transactionInfos = new ArrayList<>();
                        transactionInfos.add(transactionInfo);
                        report.put(transaction.getUser().getUsername(), transactionInfos);
                    }
                });
        return report;
    }

    public void confirmTransactions() {
        log.info("Confirming transactions");
        transactionRepository.confirmAll();
    }

    public Map<String, List<TransactionInfo>> getTransactionsGroupedByType() {
        Map<String, List<TransactionInfo>> report = new HashMap<>();
        transactionRepository.findAll()
                .forEach(transaction -> {
                    TransactionInfo transactionInfo = TransactionInfo.builder()
                            .type(transaction.getType())
                            .amount(transaction.getAmount())
                            .build();
                    if (report.containsKey(transaction.getType().toString())) {
                        report.get(transaction.getType().toString())
                                .add(transactionInfo);

                    } else {
                        List<TransactionInfo> transactionInfos = new ArrayList<>();
                        transactionInfos.add(transactionInfo);
                        report.put(transaction.getType().toString(), transactionInfos);
                    }
                });
        return report;
    }

    public Map<String, List<TransactionInfo>> getTransactionsGroupedByProduct() {
        Map<String, List<TransactionInfo>> report = new HashMap<>();
        transactionRepository.findAll()
                .forEach(transaction -> {
                    TransactionInfo transactionInfo = TransactionInfo.builder()
                            .type(transaction.getType())
                            .amount(transaction.getAmount())
                            .build();
                    if (report.containsKey(transaction.getProduct().getName())) {
                        report.get(transaction.getProduct().getName())
                                .add(transactionInfo);

                    } else {
                        List<TransactionInfo> transactionInfos = new ArrayList<>();
                        transactionInfos.add(transactionInfo);
                        report.put(transaction.getProduct().getName(), transactionInfos);
                    }
                });
        return report;
    }

    public List<Transaction> find(String sortBy, String order, Integer page, Integer size) {
        log.info("sortBy: " + sortBy + " order: " + order + " page: " + page + " size: " + size);
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.fromString(order), sortBy));
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionRepository.findAll(pageable).getContent();
    }

    public void findUsingCriteria(Criteria criteria) {
        List<Sort.Order> orders = new ArrayList<>();
        criteria.getSortOptions().forEach(sortOption -> orders
                .add(new Sort.Order(
                        Sort.Direction.fromString(sortOption.getDirection()),
                        sortOption.getProperty())));
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(criteria.getPage() - 1, criteria.getSize(), sort);
        transactionRepository.findAll(pageable).getContent();
    }

    public Page<Transaction> findUsingDao(Criteria criteria) {
        return transactionDao.findUsingCriteriaBuilder(criteria);
    }
}
