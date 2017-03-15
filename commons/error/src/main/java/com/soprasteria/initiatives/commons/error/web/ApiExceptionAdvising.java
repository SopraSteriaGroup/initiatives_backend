package com.soprasteria.initiatives.commons.error.web;

import com.soprasteria.initiatives.commons.error.exception.DEPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import rx.exceptions.CompositeException;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * Intercept exceptions thrown by resources and try to resolved them to managed exceptions
 *
 * @author jntakpe
 */
@ControllerAdvice("com.soprasteria.initiatives")
public class ApiExceptionAdvising {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public String handle(ValidationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CompositeException.class)
    public ResponseEntity<RuntimeException> handleComposite(CompositeException exception) {
        return exception.getExceptions().stream()
                .filter(e -> e.getClass().isAssignableFrom(DEPException.class))
                .map(e -> (DEPException) e)
                .findFirst()
                .map(e -> new ResponseEntity<RuntimeException>(e, HttpStatus.valueOf(e.getExceptionCode().getStatus())))
                .orElse(new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
