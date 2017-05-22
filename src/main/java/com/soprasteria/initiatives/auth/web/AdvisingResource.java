package com.soprasteria.initiatives.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common resource handling exception for all resources
 *
 * @author jntakpe
 */
@ControllerAdvice
public class AdvisingResource {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidation(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        if (e.getMessage() == null) {
            List<String> constraints = e.getConstraintViolations().stream()
                    .map(constraintViolation -> new StringBuffer()
                            .append("Le champ ")
                            .append(constraintViolation.getPropertyPath().toString())
                            .append(" ")
                            .append(constraintViolation.getMessage())
                            .append(" (valeur : ")
                            .append(constraintViolation.getInvalidValue().toString())
                            .append(")")
                            .toString())
                    .collect(Collectors.toList());
            return new ResponseEntity<>(constraints, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Collections.singletonList(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}