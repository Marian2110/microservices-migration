package ro.fasttrackit.budgetapplication.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FilterOption {
    private String fieldName;
    private String value;
    private String operator;

}
