package com.sporekart.ai.automation.application;

import com.sporekart.ai.automation.api.AutomationAuditService;
import com.sporekart.ai.automation.domain.AutomationAudit;
import com.sporekart.ai.automation.infrastructure.persistence.AutomationAuditRepository;
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
public class AutomationAuditServiceImpl implements AutomationAuditService {

    private final AutomationAuditRepository auditRepository;

    @Override
    public void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress) {
        var audit = new AutomationAudit(
            UUID.randomUUID(), action, entityType, entityId,
            performedBy, details, ipAddress, Instant.now()
        );
        auditRepository.save(audit);
        log.debug("Audit recorded: {} on {} {}", action, entityType, entityId);
    }

    @Override
    public List<AutomationAudit> getAuditLogs(UUID entityId) {
        return auditRepository.findByEntityId(entityId);
    }

    @Override
    public List<AutomationAudit> getAuditLogsByDateRange(Instant from, Instant to) {
        return auditRepository.findByTimestampBetween(from, to);
    }
}
