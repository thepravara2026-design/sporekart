package com.sporekart.ai.compliance.infrastructure.persistence;

import com.sporekart.ai.compliance.domain.ComplianceAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceAuditRepository extends JpaRepository<ComplianceAudit, UUID> {
    List<ComplianceAudit> findByEntityId(UUID entityId);
    List<ComplianceAudit> findByTimestampBetween(Instant from, Instant to);
}
