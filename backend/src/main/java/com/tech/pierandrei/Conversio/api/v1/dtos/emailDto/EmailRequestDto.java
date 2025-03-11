package com.tech.pierandrei.Conversio.api.v1.dtos.emailDto;

import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Type;

import java.time.LocalDateTime;
import java.util.List;

public class EmailRequestDto {
    private String from;

    private Type type;

    private List<Clients> clients;

    private String subject;

    private String pOne;

    private String pTwo;

    private boolean hasButton;

    private String urlButton;

    private boolean hasBanner;

    private String urlBanner;

    private boolean isProgrammed;

    private LocalDateTime sendAt;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Clients> getClients() {
        return clients;
    }

    public void setClients(List<Clients> clients) {
        this.clients = clients;
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


    public boolean isHasButton(boolean hasButton) {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isHasButton() {
        return hasButton;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isProgrammed() {
        return isProgrammed;
    }

    public void setProgrammed(boolean programmed) {
        isProgrammed = programmed;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }
}
