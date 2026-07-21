package com.sporekart.ai.runtime.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AgentDefinitionResponse(
    UUID id,
    String name,
    String description,
    String type,
    String status,
    String systemPrompt,
    List<String> tools,
    Map<String, String> configuration,
    int maxIterations,
    int maxTokens,
    double temperature,
    Instant createdAt,
    Instant updatedAt,
    String version
) {}
