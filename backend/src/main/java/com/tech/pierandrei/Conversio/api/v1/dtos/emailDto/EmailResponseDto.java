package com.tech.pierandrei.Conversio.api.v1.dtos.emailDto;

import com.tech.pierandrei.Conversio.api.v1.entities.emails.StatusEnum;

import java.util.HashMap;

public class EmailResponseDto {

    private String time;
    private int quantityEmailsSelected;
    private int totalEmailsSended;
    private int quantityEmailsFailed;
    private HashMap<String, StatusEnum> toEmailsAndStatus;

    // Getters e Setters
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQuantityEmailsSelected() {
        return quantityEmailsSelected;
    }

    public void setQuantityEmailsSelected(int quantityEmailsSelected) {
        if (quantityEmailsSelected < 0) {
            throw new IllegalArgumentException("The number of emails selected cannot be negative.");
        }
        this.quantityEmailsSelected = quantityEmailsSelected;
    }

    public int getTotalEmailsSended() {
        return totalEmailsSended;
    }

    public void setTotalEmailsSended(int totalEmailsSended) {
        this.totalEmailsSended = totalEmailsSended;
    }

    public int getQuantityEmailsFailed() {
        if (totalEmailsSended < 0) {
            throw new IllegalArgumentException("The number of emails selected cannot be negative.");
        }
        return quantityEmailsFailed;
    }

    public void setQuantityEmailsFailed(int quantityEmailsFailed) {
        if (quantityEmailsFailed < 0) {
            throw new IllegalArgumentException("The number of emails selected cannot be negative.");
        }
        this.quantityEmailsFailed = quantityEmailsFailed;
    }

    public HashMap<String, StatusEnum> getToEmailsAndStatus() {
        return toEmailsAndStatus;
    }

    public void setToEmailsAndStatus(HashMap<String, StatusEnum> toEmailsAndStatus) {
        this.toEmailsAndStatus = toEmailsAndStatus;
    }
}
