package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfigurationSnapshotRepository extends JpaRepository<ConfigurationSnapshotEntity, UUID> {
    List<ConfigurationSnapshotEntity> findByEnvironment(String environment);
    Optional<ConfigurationSnapshotEntity> findByName(String name);
}
