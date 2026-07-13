package com.sporekart.ai.compliance.application;

import com.sporekart.ai.compliance.domain.ComplianceAudit;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ComplianceAuditService {
    ComplianceAudit recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress);
    List<ComplianceAudit> getAuditLogs(UUID entityId);
    List<ComplianceAudit> getAuditLogsByDateRange(Instant from, Instant to);
}
