package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class RolesNotFoundException extends RuntimeException{
    public RolesNotFoundException(){super("Role Not Found!");}

    public RolesNotFoundException(String message){super(message);}
}
