package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface RiskAuditService {
    void recordAudit(String action, String entityType, UUID entityId, UUID performedBy, Map<String, Object> details, String ipAddress);
    List<RiskAudit> getAuditLogs(UUID entityId);
    List<RiskAudit> getAuditLogsByDateRange(Instant from, Instant to);
}
