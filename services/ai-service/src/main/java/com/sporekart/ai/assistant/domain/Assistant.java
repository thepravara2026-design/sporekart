package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record Assistant(
    UUID id,
    String name,
    String description,
    AssistantType type,
    AssistantStatus status,
    Map<String, Object> config,
    boolean isActive,
    UUID createdBy,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
