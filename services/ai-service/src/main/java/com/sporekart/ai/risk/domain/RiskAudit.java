package com.sporekart.ai.risk.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskAudit(
    UUID id,
    String action,
    String entityType,
    UUID entityId,
    UUID performedBy,
    Map<String, Object> details,
    String ipAddress,
    Instant timestamp
) {}
