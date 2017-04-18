package com.soprasteria.initiatives.user.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ValidationException;

/**
 * Common resource handling exception for all resources
 *
 * @author jntakpe
 * @author cegiraud
 */
@ControllerAdvice
public class AdvisingResource {

    @ExceptionHandler({NoResultException.class, EntityNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleNotFound(PersistenceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidation(ValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
