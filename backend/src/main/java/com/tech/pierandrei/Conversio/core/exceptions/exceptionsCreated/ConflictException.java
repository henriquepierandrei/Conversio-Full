package com.tech.pierandrei.Conversio.core.exceptions.exceptionsCreated;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Email already exists.");
    }

    public ConflictException(String message) {
        super(message);
    }
}