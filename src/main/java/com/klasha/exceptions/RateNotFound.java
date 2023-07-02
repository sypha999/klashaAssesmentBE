package com.klasha.exceptions;

public class RateNotFound extends RuntimeException{
    public RateNotFound(String message){
        super(message);
    }
}