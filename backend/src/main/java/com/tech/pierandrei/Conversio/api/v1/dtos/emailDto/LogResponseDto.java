package com.tech.pierandrei.Conversio.api.v1.dtos.emailDto;

import com.tech.pierandrei.Conversio.api.v1.entities.emails.StatusEnum;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class LogResponseDto {

    private String smtpUsernameOwner;

    private Instant createdAt;

    private String templateName;

    private HashMap<String, StatusEnum> reportEmailsSended;

    private String timeToSend;

    private boolean hadError;

    private int quantityEmails;

    private String messageError;


    // GETTERS AND SETTERS

    public String getSmtpUsernameOwner() {
        return smtpUsernameOwner;
    }

    public void setSmtpUsernameOwner(String smtpUsernameOwner) {
        this.smtpUsernameOwner = smtpUsernameOwner;
    }

    public String getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(createdAt);
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public HashMap<String, StatusEnum> getReportEmailsSended() {
        return reportEmailsSended;
    }

    public void setReportEmailsSended(HashMap<String, StatusEnum> reportEmailsSended) {
        this.reportEmailsSended = reportEmailsSended;
    }

    public String getTimeToSend() {
        return timeToSend;
    }

    public void setTimeToSend(String timeToSend) {
        this.timeToSend = timeToSend;
    }

    public boolean isHadError() {
        return hadError;
    }

    public void setHadError(boolean hadError) {
        this.hadError = hadError;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public int getQuantityEmails() {
        return quantityEmails;
    }

    public void setQuantityEmails(int quantityEmails) {
        this.quantityEmails = quantityEmails;
    }
}
