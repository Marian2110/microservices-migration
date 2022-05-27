package ro.fasttrackit.budgetapplication.exception;

import java.util.List;

public class BadRequestException extends RuntimeException {
    private List<String> fields;
    public BadRequestException(String message) {
        super(message);
    }
}
