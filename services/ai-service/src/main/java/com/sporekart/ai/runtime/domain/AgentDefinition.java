package com.sporekart.ai.runtime.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AgentDefinition(
    UUID id,
    String name,
    String description,
    AgentType type,
    AgentStatus status,
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
