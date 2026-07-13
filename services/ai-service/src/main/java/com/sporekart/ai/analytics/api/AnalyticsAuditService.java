package com.sporekart.ai.analytics.api;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AnalyticsAuditService {
    void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress);
    List<Object> getAuditLogs(UUID entityId);
    List<Object> getAuditLogsByDateRange(Instant from, Instant to);
}
