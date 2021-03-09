package com.github.suloginscene.authserver.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;


@Data
@AllArgsConstructor(access = PRIVATE)
public class ErrorResponse {

    private final String error;
    private final String errorDescription;


    public static ErrorResponse of(Exception e) {
        String className = e.getClass().getSimpleName();
        String message = e.getMessage();
        return new ErrorResponse(className, message);
    }

    public static ErrorResponse of(BindException e) {
        String className = e.getClass().getSimpleName();
        String message = e.getBindingResult()
                .getFieldErrors().stream()
                .map(ErrorResponse::getFormat)
                .collect(Collectors.joining(", "));
        return new ErrorResponse(className, message);
    }

    private static String getFormat(FieldError r) {
        return String.format("'%s' is rejected for '%s'(%s)",
                r.getRejectedValue(), r.getField(), r.getDefaultMessage());
    }

}
