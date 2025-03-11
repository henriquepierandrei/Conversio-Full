package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super("Invalid emailDto or password.");
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
