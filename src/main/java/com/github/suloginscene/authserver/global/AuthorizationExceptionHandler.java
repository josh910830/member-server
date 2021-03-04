package com.github.suloginscene.authserver.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@ControllerAdvice
@Slf4j
public class AuthorizationExceptionHandler {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> on(AuthorizationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return forbiddenWithLogWarn(errorResponse);
    }

    private ResponseEntity<ErrorResponse> forbiddenWithLogWarn(ErrorResponse errorResponse) {
        log.warn(errorResponse.toString());
        return ResponseEntity.status(FORBIDDEN).body(errorResponse);
    }

}
