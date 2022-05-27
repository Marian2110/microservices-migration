package ro.fasttrackit.budgetapplication.utils;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TransactionInfo {
    private TransactionType type;
    private Double amount;
}
