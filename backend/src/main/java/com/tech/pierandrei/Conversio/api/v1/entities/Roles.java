package com.tech.pierandrei.Conversio.api.v1.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Roles() {}

    public Roles(String name) {
        this.name = name;
    }

    public enum RoleValues {
        SUPERADMIN("SUPERADMIN"),
        ADMIN("ADMIN"),
        MODERATOR("MODERATOR"),
        SUPPORT("SUPPORT"),
        USER("USER");

        private final String roleName;

        RoleValues(String roleName) {
            this.roleName = roleName;
        }

    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}