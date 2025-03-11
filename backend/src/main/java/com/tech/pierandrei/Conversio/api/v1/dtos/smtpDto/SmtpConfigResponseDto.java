package com.tech.pierandrei.Conversio.api.v1.dtos.smtpDto;

public class SmtpConfigResponseDto {
    long id;
    private String host;
    private Boolean auth;
    private Boolean starttls;
    private int port;
    private String username;
    private String password;
    private String sslTrust;

    // GETTERS AND SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
        return "************";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSslTrust() {
        return sslTrust;
    }

    public void setSslTrust(String sslTrust) {
        this.sslTrust = sslTrust;
    }
}
