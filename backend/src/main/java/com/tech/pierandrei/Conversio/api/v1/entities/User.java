package com.tech.pierandrei.Conversio.api.v1.entities;

import com.tech.pierandrei.Conversio.api.v1.dtos.authDto.LoginRequest;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Email;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "user_id")
    private UUID uuid;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Clients> clients;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Email> emailsSent;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Column(nullable = false)
    private Set<Roles> role;

    @Column(nullable = false)
    private String hashPassword;

    private Instant createdAt;

    private Instant lastLoginAt;

    private boolean useAnotherSmtpWhenExpires = false;

    // GETTERS AND SETTERS


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Clients> getClients() {
        return clients;
    }

    public void setClients(List<Clients> clients) {
        this.clients = clients;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Instant lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Set<Roles> getRole() {
        return role;
    }

    public void setRole(Set<Roles> role) {
        this.role = role;
    }

    public boolean isApprovedLogin(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        if (loginRequest.password() == null || this.hashPassword == null) {
            return false;
        }
        boolean matches = passwordEncoder.matches(loginRequest.password(), this.hashPassword);
        return matches;
    }

    public List<Email> getEmailsSent() {
        return emailsSent;
    }

    public void setEmailsSent(List<Email> emailsSent) {
        this.emailsSent = emailsSent;
    }

    public boolean isUseAnotherSmtpWhenExpires() {
        return useAnotherSmtpWhenExpires;
    }

    public void setUseAnotherSmtpWhenExpires(boolean useAnotherSmtpWhenExpires) {
        this.useAnotherSmtpWhenExpires = useAnotherSmtpWhenExpires;
    }
}
