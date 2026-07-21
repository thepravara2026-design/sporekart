package com.sporekart.ai.runtime.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record AgentScheduleRequest(
    @NotBlank String agentId,
    @NotBlank String cronExpression,
    String timezone,
    boolean enabled
) {}
