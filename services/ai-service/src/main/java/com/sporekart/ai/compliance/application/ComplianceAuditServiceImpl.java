package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAudit;
import com.sporekart.ai.compliance.infrastructure.persistence.ComplianceAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceAuditServiceImpl implements ComplianceAuditService {

    private final ComplianceAuditRepository auditRepository;

    @Override
    public ComplianceAudit recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress) {
        ComplianceAudit audit = new ComplianceAudit(
            UUID.randomUUID(),
            action,
            entityType,
            entityId,
            performedBy,
            details != null ? details : Map.of(),
            ipAddress,
            Instant.now()
        );
        ComplianceAudit saved = auditRepository.save(audit);
        log.debug("Recorded audit: {} {} {}", action, entityType, entityId);
        return saved;
    }

    @Override
    public List<ComplianceAudit> getAuditLogs(UUID entityId) {
        return auditRepository.findByEntityId(entityId);
    }

    @Override
    public List<ComplianceAudit> getAuditLogsByDateRange(Instant from, Instant to) {
        return auditRepository.findByTimestampBetween(from, to);
    }
}
