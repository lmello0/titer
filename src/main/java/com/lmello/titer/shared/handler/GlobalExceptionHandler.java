package com.lmello.titer.shared.handler;

import com.lmello.titer.shared.exception.DomainException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Invalid request payload");
        problem.setDetail("One or more fields are invalid");

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                FieldError::getDefaultMessage,
                                (first, second) -> first
                        )
                );

        problem.setProperty("errors", errors);

        return ResponseEntity
                .badRequest()
                .body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleUnreadableBody(HttpMessageNotReadableException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Malformed request body");

        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            problem.setDetail("Invalid value");

            problem.setProperty("field", ife.getPath().isEmpty()
                    ? null
                    : ife.getPath().getLast().getPropertyName());

            problem.setProperty("rejectedValue", ife.getValue());

            problem.setProperty("allowedValues",
                    Arrays.stream(ife.getTargetType().getEnumConstants())
                            .map(Object::toString)
                            .toList()
            );

            return ResponseEntity.badRequest().body(problem);
        }

        problem.setDetail("Request body is not valid JSON or cannot be parsed");
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            DomainException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid request");
        problem.setDetail(ex.getMessage());

        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal server error");
        problem.setDetail("Unexpected error");

        return ResponseEntity.internalServerError().body(problem);
    }
}
