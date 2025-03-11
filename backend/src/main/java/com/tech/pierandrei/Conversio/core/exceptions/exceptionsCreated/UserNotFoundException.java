package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){super("User Not Found.");}

    public UserNotFoundException(String message){super(message);}
}
