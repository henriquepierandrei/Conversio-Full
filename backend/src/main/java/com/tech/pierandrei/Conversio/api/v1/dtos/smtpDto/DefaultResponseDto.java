package com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto;

public class DefaultResponseDto {
    public String message;
    private int status;

    public DefaultResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }

    // GETTERS AND SETTERS

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
