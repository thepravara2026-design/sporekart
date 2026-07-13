package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.UUID;

public record TransitionDto(
    UUID entityId,
    String entityType,
    String targetState,
    String triggeredBy,
    String reason
) {}
