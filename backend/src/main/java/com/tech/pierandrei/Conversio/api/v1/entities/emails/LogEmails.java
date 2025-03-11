package com.tech.pierandrei.Conversio.api.v1.entities.emails;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.pierandrei.Conversio.core.configs.HashMapJsonConverter;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Entity
@Table(name = "tb_log_emails")
public class LogEmails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id")
    private UUID id;

    private String smtpUsernameOwner;

    private Instant createdAt;

    private String templateName;

    @Convert(converter = HashMapJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private HashMap<String, StatusEnum> reportEmailsSended;

    private String timeToSend;

    private boolean hadError;

    private String messageError;

    private int quantityEmails;

    // GETTERS AND SETTERS

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSmtpUsernameOwner() {
        return smtpUsernameOwner;
    }

    public void setSmtpUsernameOwner(String smtpUsernameOwner) {
        this.smtpUsernameOwner = smtpUsernameOwner;
    }

    public Instant getCreatedAt() {
        return createdAt;
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
