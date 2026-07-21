package com.sporekart.ai.runtime.domain;

import java.time.Instant;
import java.util.UUID;

public record AgentEvent(
    UUID id,
    String agentId,
    String executionId,
    AgentEventType type,
    String data,
    Instant timestamp
) {}
