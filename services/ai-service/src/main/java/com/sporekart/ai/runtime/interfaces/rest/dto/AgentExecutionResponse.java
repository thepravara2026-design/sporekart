package com.sporekart.ai.runtime.interfaces.rest.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AgentExecutionResponse(
    UUID id,
    UUID agentId,
    String sessionId,
    String userId,
    String status,
    String input,
    String output,
    Map<String, String> metadata,
    int iterationsUsed,
    int tokensUsed,
    Instant startedAt,
    Instant completedAt,
    String errorMessage
) {}
