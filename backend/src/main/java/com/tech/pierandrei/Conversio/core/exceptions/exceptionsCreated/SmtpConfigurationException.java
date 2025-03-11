package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class SmtpConfigurationException extends RuntimeException{
    public SmtpConfigurationException(){super("SMTP configuration error!");}

    public SmtpConfigurationException(String message){super(message);}
}
