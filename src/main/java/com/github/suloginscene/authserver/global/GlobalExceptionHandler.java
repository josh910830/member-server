package com.github.suloginscene.authserver.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> on(AuthorizationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        log.warn(errorResponse.toString());
        return ResponseEntity.status(FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> on(NoSuchElementException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        log.warn(errorResponse.toString());
        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

}
