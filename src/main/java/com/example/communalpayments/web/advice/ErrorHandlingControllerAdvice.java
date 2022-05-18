package com.example.communalpayments.web.advice;

import com.example.communalpayments.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return e.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }

    @ExceptionHandler({TemplateNotFoundException.class,
            PaymentNotFoundException.class, UserNotFoundException.class, AddressNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onMethodNotFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler({PaymentDuplicateException.class, UserEmailExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onMethodOtherException(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }
}
