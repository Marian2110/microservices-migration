package ro.fasttrackit.budgetapplication.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class Criteria {
    @Min(value = 0, message = "Page number must be greater than 0")
    private Integer page;
    @Min(value = 1, message = "Page size must be greater than 0")
    private Integer size;
    @JsonProperty("sortOptions")
    private List<SortOption> sortOptions;
    private List<FilterOption> filterOptions;

}
