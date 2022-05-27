package ro.fasttrackit.budgetapplication.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.xml.bind.v2.TODO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Long id;

    @NotBlank
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    /*
    ^                 # start-of-string
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once
    (?=\\S+$)         # no whitespace allowed in the entire string
    .{8,}             # anything, at least eight places though
    $                 # end-of-string
     */
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password is not complex enough")
    private String password;

    private List<String> roles;

}
