package ro.fasttrackit.budgetapplication.service.transaction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ro.fasttrackit.budgetapplication.utils.Criteria;
import ro.fasttrackit.budgetapplication.utils.SortOption;
import ro.fasttrackit.budgetapplication.utils.TransactionInfo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class TransactionServiceTest {
    @Spy
    @InjectMocks
    TransactionService transactionService;

    @Test
    void testFindUsingDao() {

        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        TransactionDao transactionDao = Mockito.mock(TransactionDao.class);

        TransactionService transactionService = Mockito.spy(new TransactionService(transactionRepository, transactionDao));

        //given
        List<SortOption> sortOptions = List.of(
                SortOption.builder().direction("asc").property("id").build(),
                SortOption.builder().direction("desc").property("amount").build()
        );

        Criteria criteria = Criteria.builder()
                .page(1)
                .size(10)
                .sortOptions(sortOptions)
                .build();

        Mockito.when(transactionRepository.findAll((Pageable) Mockito.any())).thenReturn(Page.empty());

        //when
        transactionService.findUsingCriteria(criteria);

        //then
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(transactionRepository).findAll(captor.capture());
        Pageable pageable = captor.getValue();
        Assertions.assertThat(pageable.getPageNumber()).isEqualTo(criteria.getPage() - 1);
        Assertions.assertThat(pageable.getPageSize()).isEqualTo(criteria.getSize());
        Assertions.assertThat(
                        Objects.requireNonNull(pageable.getSort().getOrderFor("id")).getDirection())
                .isEqualTo(Sort.Direction.fromString(sortOptions.get(0).getDirection()));
    }

    @Test
    @PrepareForTest(TransactionService.class)
    void testGenerateReport() throws Exception {
        //given

        TransactionRepository transactionRepository = PowerMockito.mock(TransactionRepository.class);
        TransactionDao transactionDao = PowerMockito.mock(TransactionDao.class);

        TransactionService transactionService = PowerMockito
                .spy(new TransactionService(transactionRepository, transactionDao));

//        LocalDateTime firstDate = LocalDateTime.now();
//        LocalDateTime secondDate = LocalDateTime.now();
        String path = "path";

        HashMap<String, List<TransactionInfo>> report = new HashMap<>();
        PowerMockito.doReturn(report)
                .when(transactionService, "mapTransactionsToUsers", ArgumentMatchers.any(), ArgumentMatchers.any());

        PowerMockito.doNothing()
                .when(transactionService, "writeFile", path, report);

        //when
        transactionService.generateReport(path);

        //then
        PowerMockito.verifyPrivate(transactionService).invoke("mapTransactionsToUsers", ArgumentMatchers.any(), ArgumentMatchers.any());
        PowerMockito.verifyPrivate(transactionService).invoke("writeFile", path, report);

        /*
         * If mocking of private methods is essential for testing our classes, it usually indicates a bad design.
         */
    }
}