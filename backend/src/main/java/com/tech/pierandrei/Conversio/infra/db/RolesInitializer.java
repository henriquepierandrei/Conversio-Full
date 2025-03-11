package com.tech.pierandrei.Conversio.infra.db;

import com.tech.pierandrei.Conversio.api.v1.entities.Roles;
import com.tech.pierandrei.Conversio.api.v1.repositories.RolesRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolesInitializer {

    private final RolesRepository rolesRepository;

    @Autowired
    public RolesInitializer(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @PostConstruct
    public void initializeRoles() {
        addRoleIfNotExists(Roles.RoleValues.SUPERADMIN);
        addRoleIfNotExists(Roles.RoleValues.ADMIN);
        addRoleIfNotExists(Roles.RoleValues.MODERATOR);
        addRoleIfNotExists(Roles.RoleValues.SUPPORT);
        addRoleIfNotExists(Roles.RoleValues.USER);
    }

    private void addRoleIfNotExists(Roles.RoleValues roleValue) {
        String roleName = roleValue.name();
        if (!rolesRepository.existsByName(roleName)) {
            Roles role = new Roles(roleName);
            rolesRepository.save(role);
        }
    }
}