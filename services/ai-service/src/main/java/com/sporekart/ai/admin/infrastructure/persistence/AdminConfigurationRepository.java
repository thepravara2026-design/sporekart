package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminConfigurationRepository extends JpaRepository<AdminConfigurationEntity, UUID> {
    Optional<AdminConfigurationEntity> findByKeyAndModuleAndEnvironment(String key, String module, String environment);
    List<AdminConfigurationEntity> findByModule(String module);
    List<AdminConfigurationEntity> findByEnvironment(String environment);
    Optional<AdminConfigurationEntity> findByKey(String key);
}
