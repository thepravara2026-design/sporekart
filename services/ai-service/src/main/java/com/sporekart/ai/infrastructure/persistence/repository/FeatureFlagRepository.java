package com.sporekart.ai.infrastructure.persistence.repository;

import com.sporekart.ai.infrastructure.persistence.entity.FeatureFlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlagEntity, UUID> {
    boolean existsByFeatureKey(String featureKey);
}
