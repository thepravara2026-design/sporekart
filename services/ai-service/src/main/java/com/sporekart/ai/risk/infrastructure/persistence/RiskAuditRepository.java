package com.sporekart.ai.risk.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RiskAuditRepository extends JpaRepository<RiskAuditEntity, UUID> {
    List<RiskAuditEntity> findByEntityId(UUID entityId);
    List<RiskAuditEntity> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
