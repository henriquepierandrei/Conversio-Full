package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.entities.User;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.Clients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Clients, UUID> {
    Optional<Clients> findByUuidAndUserOwner(UUID uuid, User user);

    Optional<Clients> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Clients> findByUserOwner(User user);

    Page<Clients> findByUserOwner(User user, Pageable pageable);
}
