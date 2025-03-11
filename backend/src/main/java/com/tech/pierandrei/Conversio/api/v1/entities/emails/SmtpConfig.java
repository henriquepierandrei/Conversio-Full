package com.tech.pierandrei.Conversio.api.v1.entities.emails;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_smtp_configs")
public class SmtpConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "smtp_account_id")
    private Long id;

    private String host;
    private int port;
    private String username;
    private String password;
    private boolean auth;
    private boolean starttls;
    private String sslTrust;

    private int quantityEmailSentToday;
    private int totalQuantityEmailsSents;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isStarttls() {
        return starttls;
    }

    public void setStarttls(boolean starttls) {
        this.starttls = starttls;
    }

    public String getSslTrust() {
        return sslTrust;
    }

    public void setSslTrust(String sslTrust) {
        this.sslTrust = sslTrust;
    }

    public int getTotalQuantityEmailsSents() {
        return totalQuantityEmailsSents;
    }

    public void setTotalQuantityEmailsSents(int totalQuantityEmailsSents) {
        this.totalQuantityEmailsSents = totalQuantityEmailsSents;
    }

    public int getQuantityEmailSentToday() {
        return quantityEmailSentToday;
    }

    public void setQuantityEmailSentToday(int quantityEmailSentToday) {
        this.quantityEmailSentToday = quantityEmailSentToday;
    }


}
