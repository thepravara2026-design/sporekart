package com.sporekart.ai.workflow.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowScheduleRequest(
        @NotNull UUID workflowId,
        @NotBlank String cronExpression,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        String timezone
) {}
