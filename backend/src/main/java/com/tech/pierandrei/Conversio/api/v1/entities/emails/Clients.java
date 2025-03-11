package com.tech.pierandrei.Conversio.api.v1.entities.emails;

import com.tech.pierandrei.Conversio.api.v1.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_clients")
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, name = "client_id")
    private UUID uuid;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userOwner;


    @NotBlank
    private String name;

    private String email;

    private Instant createdAt;

    private Instant updatedAt;

//    @ManyToMany(mappedBy = "toClients")
//    private List<EmailProgrammed> emailProgrammeds;


    // GETTERS AND SETTERS


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }


}
