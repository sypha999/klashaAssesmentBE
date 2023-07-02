package com.klasha.exceptions;

public class NotFound extends RuntimeException{
    public NotFound(String message){
        super(message);
    }
}