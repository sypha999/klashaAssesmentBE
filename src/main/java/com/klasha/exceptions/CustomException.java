package com.klasha.exceptions;

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}