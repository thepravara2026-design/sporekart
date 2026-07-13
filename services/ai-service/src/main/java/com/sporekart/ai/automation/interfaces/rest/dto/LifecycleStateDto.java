package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;
import java.util.UUID;

public record LifecycleStateDto(
    String id,
    UUID entityId,
    String entityType,
    String currentState,
    Map<String, Object> metadata,
    String enteredAt
) {}
