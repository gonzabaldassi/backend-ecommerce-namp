package com.namp.ecommerce.error;

public class InvalidFileFormatException extends RuntimeException{
    public InvalidFileFormatException(String message){
        super(message);
    }
}

