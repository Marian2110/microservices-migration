package ro.fasttrackit.budgetapplication.service.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.fasttrackit.budgetapplication.model.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByConfirmedTrueAndCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t " +
            "WHERE (:startDate is null and :endDate is null or t.createdAt BETWEEN :startDate AND :endDate)" +
            "AND (:startDate is null or t.createdAt > :startDate)" +
            "AND (:endDate is null or t.createdAt < :endDate)" +
            "AND (:confirmed is null or t.confirmed = :confirmed)" +
            "AND (:type is null or t.type = :type)" +
            "AND (:amount is null or t.amount = :amount)")
    Page<Transaction> findAll(@Param("startDate") LocalDateTime startDate,
                              @Param("endDate") LocalDateTime endDate,
                              @Param("confirmed") Boolean confirmed,
                              @Param("type") String type,
                              @Param("amount") Double amount,
                              Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.confirmed = true WHERE t.confirmed = false")
    void confirmAll();
}
