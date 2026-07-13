package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.AutomationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AutomationAuditRepository extends JpaRepository<AutomationAudit, UUID> {
    List<AutomationAudit> findByEntityId(UUID entityId);
    List<AutomationAudit> findByTimestampBetween(Instant from, Instant to);
}
