package com.tech.pierandrei.Conversio.api.v1.dtos.emailDto;

public class DashboardResponseDto {
    private Integer quantityEmailsIn24h;
    private Integer totalEmailsSended;
    private Integer totalClients;
    private Integer fullEmailCapicity;

    public DashboardResponseDto(Integer quantityEmailsIn24h, Integer totalEmailsSended, Integer totalClients, Integer fullEmailCapicity) {
        this.quantityEmailsIn24h = quantityEmailsIn24h;
        this.totalEmailsSended = totalEmailsSended;
        this.totalClients = totalClients;
        this.fullEmailCapicity = fullEmailCapicity;
    }

    // GETTERS AND SETTERS


    public Integer getQuantityEmailsIn24h() {
        return quantityEmailsIn24h;
    }

    public void setQuantityEmailsIn24h(Integer quantityEmailsIn24h) {
        this.quantityEmailsIn24h = quantityEmailsIn24h;
    }

    public Integer getTotalEmailsSended() {
        return totalEmailsSended;
    }

    public void setTotalEmailsSended(Integer totalEmailsSended) {
        this.totalEmailsSended = totalEmailsSended;
    }


    public Integer getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(Integer totalClients) {
        this.totalClients = totalClients;
    }

    public Integer getFullEmailCapicity() {
        return fullEmailCapicity;
    }

    public void setFullEmailCapicity(Integer fullEmailCapicity) {
        this.fullEmailCapicity = fullEmailCapicity;
    }
}
