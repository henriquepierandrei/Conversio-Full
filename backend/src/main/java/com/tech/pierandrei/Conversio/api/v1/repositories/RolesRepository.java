package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    boolean existsByName(String roleName);

    Optional<Roles> findByName(String name);
}
