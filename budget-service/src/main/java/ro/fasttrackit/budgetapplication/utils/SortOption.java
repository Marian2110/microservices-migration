package ro.fasttrackit.budgetapplication.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SortOption {
    private String direction;
    private String property;

}
