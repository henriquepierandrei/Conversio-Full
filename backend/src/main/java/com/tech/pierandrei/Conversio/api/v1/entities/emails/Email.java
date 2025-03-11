package com.tech.pierandrei.Conversio.api.v1.entities.emails;

import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.core.configs.HashMapJsonConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tb_emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "email_id", nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userOwner;

    @Convert(converter = HashMapJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private HashMap<String, StatusEnum> toEmailsAndStatus;


    @Enumerated(EnumType.STRING)
    private Type type;


    private String fromEmail;

    private String subject;

    private String pOne;

    private String pTwo;

    private boolean hasButton;

    private String urlButton;

    private boolean hasBanner;

    private String urlBanner;

    private int quantitySent;

    // Getters and Setters
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public HashMap<String, StatusEnum> getToEmailsAndStatus() {
        return toEmailsAndStatus;
    }

    public void setToEmailsAndStatus(HashMap<String, StatusEnum> toEmailsAndStatus) {
        this.toEmailsAndStatus = toEmailsAndStatus;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getQuantitySent() {
        return quantitySent;
    }

    public void setQuantitySent(int quantitySent) {
        this.quantitySent = quantitySent;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getpOne() {
        return pOne;
    }

    public void setpOne(String pOne) {
        this.pOne = pOne;
    }

    public String getpTwo() {
        return pTwo;
    }

    public void setpTwo(String pTwo) {
        this.pTwo = pTwo;
    }

    public boolean isHasButton() {
        return hasButton;
    }


    public void setHasButton(boolean hasButton) {
        this.hasButton = hasButton;
    }

    public String getUrlButton() {
        return urlButton;
    }

    public void setUrlButton(String urlButton) {
        this.urlButton = urlButton;
    }

    public boolean isHasBanner() {
        return hasBanner;
    }

    public void setHasBanner(boolean hasBanner) {
        this.hasBanner = hasBanner;
    }

    public String getUrlBanner() {
        return urlBanner;
    }

    public void setUrlBanner(String urlBanner) {
        this.urlBanner = urlBanner;
    }

}
