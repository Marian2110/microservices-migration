package ro.fasttrackit.budgetapplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 20, message = "Role name must be between 3 and 20 characters")
    private String name;
}
