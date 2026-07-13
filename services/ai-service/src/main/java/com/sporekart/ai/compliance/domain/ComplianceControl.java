package com.sporekart.ai.compliance.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ComplianceControl(
    UUID id,
    String controlId,
    String name,
    String description,
    ControlType type,
    Map<String, Object> configuration,
    boolean active,
    Instant createdAt
) {}
