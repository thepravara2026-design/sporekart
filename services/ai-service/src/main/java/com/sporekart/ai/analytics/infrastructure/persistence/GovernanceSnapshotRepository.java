package com.sporekart.ai.analytics.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface GovernanceSnapshotRepository extends JpaRepository<GovernanceSnapshotEntity, UUID> {
    List<GovernanceSnapshotEntity> findByName(String name);
    List<GovernanceSnapshotEntity> findByCapturedAtBetween(LocalDateTime from, LocalDateTime to);
}
