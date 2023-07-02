package com.klasha.exceptions;

public class CountryNotFound extends RuntimeException {
    public CountryNotFound(String message){
        super(message);
    }
}