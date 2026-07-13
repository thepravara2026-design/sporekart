package com.sporekart.ai.admin.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlagEntity, UUID> {
    Optional<FeatureFlagEntity> findByKey(String key);
    List<FeatureFlagEntity> findByModule(String module);
    List<FeatureFlagEntity> findByEnvironment(String environment);
    List<FeatureFlagEntity> findByEnabledTrue();
}
