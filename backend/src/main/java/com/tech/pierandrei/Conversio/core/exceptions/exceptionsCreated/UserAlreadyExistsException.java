package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){super("User already exists!");}

    public UserAlreadyExistsException(String message){super(message);}
}
