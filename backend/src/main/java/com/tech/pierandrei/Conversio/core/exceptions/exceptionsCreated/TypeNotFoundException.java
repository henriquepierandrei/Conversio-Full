package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class TypeNotFoundException extends RuntimeException{
    public TypeNotFoundException(){super("Type not found!");}

    public TypeNotFoundException(String message){super(message);}
}
