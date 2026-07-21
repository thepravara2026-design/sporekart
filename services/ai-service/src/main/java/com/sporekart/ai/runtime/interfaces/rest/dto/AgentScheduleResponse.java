package com.sporekart.ai.runtime.interfaces.rest.dto;

import java.time.Instant;

public record AgentScheduleResponse(
    String agentId,
    String cronExpression,
    String timezone,
    boolean enabled,
    Instant lastRunAt,
    Instant nextRunAt
) {}
