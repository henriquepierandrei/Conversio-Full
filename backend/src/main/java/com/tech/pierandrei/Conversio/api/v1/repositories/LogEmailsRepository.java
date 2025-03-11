package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.dtos.emailDto.LogResponseDto;
import com.tech.pierandrei.Conversio.api.v1.entities.emails.LogEmails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface LogEmailsRepository extends JpaRepository<LogEmails, UUID> {
    Page<LogEmails> findBySmtpUsernameOwner(String username, Pageable pageable);
    List<LogEmails> findByCreatedAtAfter(Instant twentyFourHoursAgo);
}
