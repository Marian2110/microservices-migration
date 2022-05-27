package ro.fasttrackit.budgetapplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class TransactionDTO {
    private Long id;

    @NotNull
    @NotBlank(message = "Wrong type")
    private String type;

    @NotNull
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull
    @Min(1)
    private Integer productId;

    @NotNull
    @Min(1)
    private Integer userId;
}
