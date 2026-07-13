package com.sporekart.ai.admin.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConfigurationAudit(
    UUID id,
    String action,
    String entityType,
    UUID entityId,
    UUID performedBy,
    Map<String, Object> details,
    String ipAddress,
    Instant timestamp
) {}
