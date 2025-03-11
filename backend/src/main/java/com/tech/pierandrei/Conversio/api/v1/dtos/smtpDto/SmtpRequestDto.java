package com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SmtpRequestDto {
    @NotBlank
    private String host;

    private Boolean auth;

    private Boolean starttls;

    @NotNull
    private int port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String sslTrust;

    // GETTER AND SETTER

    public String getSslTrust() {
        return sslTrust;
    }

    public void setSslTrust(String sslTrust) {
        this.sslTrust = sslTrust;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public Boolean getStarttls() {
        return starttls;
    }

    public void setStarttls(Boolean starttls) {
        this.starttls = starttls;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
