package com.sporekart.ai.runtime.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AgentExecution(
    UUID id,
    UUID agentId,
    String sessionId,
    String userId,
    ExecutionStatus status,
    String input,
    String output,
    Map<String, String> metadata,
    int iterationsUsed,
    int tokensUsed,
    Instant startedAt,
    Instant completedAt,
    String errorMessage
) {}
