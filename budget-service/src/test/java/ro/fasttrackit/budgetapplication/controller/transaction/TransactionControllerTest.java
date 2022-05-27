package ro.fasttrackit.budgetapplication.controller.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ro.fasttrackit.budgetapplication.utils.Criteria;
import ro.fasttrackit.budgetapplication.utils.SortOption;

import javax.servlet.ServletContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testFindControllers() {
        ServletContext servletContext = context.getServletContext();
        assertNotNull(mockMvc);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(context.getBean("transactionController"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindWithPaginationSortingFiltering() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<SortOption> sortOptions = List.of(
                SortOption.builder().direction("desc").property("amount").build()
        );

        Criteria criteria = Criteria.builder()
                .page(0)
                .size(10)
                .sortOptions(sortOptions)
                .build();

        String json = mapper.writeValueAsString(criteria);
        System.out.println(json);

        mockMvc.perform(post("/api/v1/transactions/search")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true));

    }
}