package com.lmello.titer.auth.controllers;

import com.lmello.titer.auth.exceptions.InvalidSocialTokenException;
import com.lmello.titer.auth.exceptions.InvalidTokenException;
import com.lmello.titer.auth.exceptions.UnsupportedProviderException;
import com.lmello.titer.users.api.exceptions.EmailAlreadyExistsException;
import com.lmello.titer.users.api.exceptions.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {

    @ExceptionHandler({
            BadCredentialsException.class,
            InvalidSocialTokenException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ProblemDetail> unauthorized(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setTitle("Authentication failed");
        problem.setDetail(HttpStatus.UNAUTHORIZED.getReasonPhrase());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            UsernameAlreadyExistsException.class
    })
    public ResponseEntity<ProblemDetail> conflict(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle(ex.getMessage());
        problem.setDetail(HttpStatus.CONFLICT.getReasonPhrase());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }

    @ExceptionHandler(UnsupportedProviderException.class)
    public ResponseEntity<ProblemDetail> badRequest(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle(ex.getMessage());
        problem.setDetail(HttpStatus.BAD_REQUEST.getReasonPhrase());

        return ResponseEntity
                .status(problem.getStatus())
                .body(problem);
    }
}
