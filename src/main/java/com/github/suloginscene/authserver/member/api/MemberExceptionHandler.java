package com.github.suloginscene.authserver.member.api;

import com.github.suloginscene.authserver.member.application.DuplicateEmailException;
import com.github.suloginscene.authserver.member.domain.MemberAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
@Slf4j
public class MemberExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Void> on(DuplicateEmailException e) {
        log.warn(toMessageWithClass(e));
        // TODO return errorResponse
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Void> on(UsernameNotFoundException e) {
        log.warn(toMessageWithClass(e));
        // TODO return errorResponse
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MemberAuthenticationException.class)
    public ResponseEntity<Void> on(MemberAuthenticationException e) {
        log.warn(toMessageWithClass(e));
        // TODO return errorResponse
        return ResponseEntity.badRequest().build();
    }

    private String toMessageWithClass(Exception e) {
        return e.getClass() + ": " + e.getMessage();
    }

}