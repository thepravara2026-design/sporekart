package com.sporekart.ai.runtime.domain;

import java.time.Instant;

public record AgentSchedule(
    String agentId,
    String cronExpression,
    String timezone,
    boolean enabled,
    Instant lastRunAt,
    Instant nextRunAt
) {}
