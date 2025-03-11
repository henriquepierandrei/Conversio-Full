package com.tech.pierandrei.Conversio.api.v1.repositories;

import com.tech.pierandrei.Conversio.api.v1.entities.User;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);
}
