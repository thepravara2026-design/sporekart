package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConfigurationVersionRepository extends JpaRepository<ConfigurationVersionEntity, UUID> {
    List<ConfigurationVersionEntity> findByConfigIdOrderByVersionDesc(UUID configId);
}
