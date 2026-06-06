package com.lmello.titer.users.internal.controllers;

import com.lmello.titer.users.internal.exception.DuplicateUserException;
import com.lmello.titer.users.internal.exception.InvalidCredentialsException;
import com.lmello.titer.users.internal.exception.RoleNotFoundException;
import com.lmello.titer.users.internal.exception.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.lmello.titer.users")
public class UserExceptionHandler {

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateUser(DuplicateUserException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("User already exists");
        problem.setDetail(ex.getMessage());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleInvalidCredentials(InvalidCredentialsException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Invalid credentials");
        problem.setDetail(ex.getMessage());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problem.setTitle("Invalid credentials");
        problem.setDetail(ex.getMessage());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleRoleNotFound(RoleNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problem.setTitle("Role not found");
        problem.setDetail(ex.getMessage());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }
}
