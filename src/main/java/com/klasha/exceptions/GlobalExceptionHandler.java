package com.klasha.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RateNotFound.class)
    public ResponseEntity<String> handlerForRateNotException(final RateNotFound e) {

        return new ResponseEntity<>("Rate Not found", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CountryNotFound.class)
    public ResponseEntity<String> handlerForCountryNotFoundException(final CountryNotFound e) {

        return new ResponseEntity<>("Country Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalInputException.class)
    public ResponseEntity<String> handlerForIllegalInputException(final IllegalInputException e) {

        return new ResponseEntity<>("Invalid input please try again with a valid input", HttpStatus.BAD_REQUEST);
    }


}