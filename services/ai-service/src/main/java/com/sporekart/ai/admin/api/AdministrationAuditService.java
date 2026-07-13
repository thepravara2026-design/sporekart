package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.ConfigurationAudit;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AdministrationAuditService {
    void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress);
    List<ConfigurationAudit> getAuditLogs(UUID entityId);
    List<ConfigurationAudit> getAuditLogsByDateRange(Instant from, Instant to);
}
