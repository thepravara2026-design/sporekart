package com.sporekart.ai.assistant.interfaces.rest.dto;

import java.util.Map;
import java.util.UUID;

public record TaskRequest(
        UUID sessionId,
        UUID intentId,
        String name,
        String description,
        Map<String, Object> input
) {}
