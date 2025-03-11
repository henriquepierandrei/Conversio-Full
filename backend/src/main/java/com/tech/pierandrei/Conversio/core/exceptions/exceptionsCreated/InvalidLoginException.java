package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class InvalidLoginException extends RuntimeException{
    public InvalidLoginException(){super("Invalid Login");}

    public InvalidLoginException(String message){super(message);}
}
