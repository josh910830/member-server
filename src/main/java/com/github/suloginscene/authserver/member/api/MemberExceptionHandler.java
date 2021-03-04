package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.application.DuplicateEmailException;
import com.github.suloginscene.authserver.member.domain.MemberAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class MemberExceptionHandler {

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

    @ExceptionHandler(MemberAuthenticationException.class)
    public ResponseEntity<ErrorResponse> on(MemberAuthenticationException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e);
        return badRequestWithLogWarn(errorResponse);
    }

    private ResponseEntity<ErrorResponse> badRequestWithLogWarn(ErrorResponse errorResponse) {
        log.warn(errorResponse.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
