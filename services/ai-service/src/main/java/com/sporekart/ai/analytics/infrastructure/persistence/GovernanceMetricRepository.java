package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernanceMetricRepository extends JpaRepository<GovernanceMetricEntity, UUID> {
    List<GovernanceMetricEntity> findByName(String name);
    List<GovernanceMetricEntity> findByModule(String module);
    List<GovernanceMetricEntity> findByNameAndModule(String name, String module);
    List<GovernanceMetricEntity> findByRecordedAtBetween(LocalDateTime from, LocalDateTime to);
}
