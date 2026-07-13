package com.sporekart.ai.automation.api;

import com.sporekart.ai.automation.domain.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AutomationAuditService {
    void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress);
    List<AutomationAudit> getAuditLogs(UUID entityId);
    List<AutomationAudit> getAuditLogsByDateRange(Instant from, Instant to);
}
