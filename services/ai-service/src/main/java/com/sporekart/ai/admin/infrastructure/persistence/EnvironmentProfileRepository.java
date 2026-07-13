package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnvironmentProfileRepository extends JpaRepository<EnvironmentProfileEntity, UUID> {
    Optional<EnvironmentProfileEntity> findByName(String name);
    List<EnvironmentProfileEntity> findByType(String type);
    Optional<EnvironmentProfileEntity> findByActiveTrue();
}
