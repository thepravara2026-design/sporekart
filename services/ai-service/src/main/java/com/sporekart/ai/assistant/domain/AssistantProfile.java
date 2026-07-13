package com.sporekart.ai.assistant.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AssistantProfile(
    UUID id,
    UUID assistantId,
    String displayName,
    String welcomeMessage,
    List<String> capabilities,
    Map<String, Object> promptOverrides,
    Map<String, Object> securityConfig,
    boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
