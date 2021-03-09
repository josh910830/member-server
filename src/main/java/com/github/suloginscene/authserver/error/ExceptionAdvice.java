package com.github.suloginscene.authserver.error;

import com.github.suloginscene.authserver.member.api.ResourceAccessDeniedException;
import com.github.suloginscene.authserver.member.application.DuplicateEmailException;
import com.github.suloginscene.authserver.member.domain.MemberPasswordNotMatchedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> on(BindException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return badRequestWithLogWarn(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> on(DuplicateEmailException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return badRequestWithLogWarn(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> on(UsernameNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return badRequestWithLogWarn(errorResponse);
    }

    @ExceptionHandler(MemberPasswordNotMatchedException.class)
    public ResponseEntity<ErrorResponse> on(MemberPasswordNotMatchedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return badRequestWithLogWarn(errorResponse);
    }

    private ResponseEntity<ErrorResponse> badRequestWithLogWarn(ErrorResponse errorResponse) {
        log.warn(errorResponse.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> on(ResourceAccessDeniedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return forbiddenWithLogWarn(errorResponse);
    }

    private ResponseEntity<ErrorResponse> forbiddenWithLogWarn(ErrorResponse errorResponse) {
        log.warn(errorResponse.toString());
        return ResponseEntity.status(FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> on(NoSuchElementException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return notFoundWithLogWarn(errorResponse);
    }

    private ResponseEntity<ErrorResponse> notFoundWithLogWarn(ErrorResponse errorResponse) {
        log.warn(errorResponse.toString());
        return ResponseEntity.status(NOT_FOUND).build();
    }

}
