package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.entities.emails.SmtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SmtpConfigRepository extends JpaRepository<SmtpConfig, Long> {
    Optional<SmtpConfig> findByUsername(String username);



}
