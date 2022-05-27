package ro.fasttrackit.budgetapplication.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.fasttrackit.budgetapplication.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException exception) {

        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .message(exception.getMessage())
                .internalCode(404)
                .build();
        return handleExceptionInternal(exception,
                exceptionResponse,
                null,
                HttpStatus.NOT_FOUND,
                null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            fieldErrors.add(FieldError
                    .builder()
                    .field(fieldError.getField())
                    .errorMessage(fieldError.getDefaultMessage())
                    .build());
        });

        ExceptionResponse exceptionResponse = ExceptionResponse
                .builder()
                .message("Bad request for the following reasons:")
                .internalCode(400)
                .fieldErrors(fieldErrors)
                .build();

        return handleExceptionInternal(ex,
                exceptionResponse,
                headers,
                HttpStatus.BAD_REQUEST,
                request);
    }
}
