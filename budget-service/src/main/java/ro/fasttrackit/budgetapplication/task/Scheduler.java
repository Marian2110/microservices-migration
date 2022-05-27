package ro.fasttrackit.budgetapplication.task;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ro.fasttrackit.budgetapplication.service.transaction.TransactionService;

@Configuration
@EnableConfigurationProperties
@AllArgsConstructor
public class Scheduler {

    private final TransactionService transactionService;

    @Scheduled(cron = "${transaction.confirm-transaction.cron}")
    public void confirmTransactions() {
        transactionService.confirmTransactions();
    }

    @Transactional
    @Scheduled(cron = "${transaction.generate-report.cron}")
    public void generateConfirmedTransactionsReport() {
        transactionService.generateReport("C:/Users/maria/");
    }

}
