package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.Map;

public record AuditEntryDto(
    String id,
    String action,
    String entityType,
    String entityId,
    String performedBy,
    Map<String, Object> details,
    String timestamp
) {}
