package com.klasha.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<String> handlerForfailedException(final NotFound e) {

        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

}