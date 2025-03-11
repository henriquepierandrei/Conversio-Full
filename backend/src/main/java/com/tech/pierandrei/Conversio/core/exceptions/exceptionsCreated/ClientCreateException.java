package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class ClientCreateException extends RuntimeException{
    public ClientCreateException(){super("Erro na criação do cliente!");}

    public ClientCreateException(String message){super(message);}
}
