package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.entities.emails.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
}
