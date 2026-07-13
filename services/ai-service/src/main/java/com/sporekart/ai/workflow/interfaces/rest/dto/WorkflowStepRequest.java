package com.sporekart.ai.workflow.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkflowStepRequest(
        @NotBlank String name,
        @NotNull String stepType,
        int orderIndex,
        String config,
        boolean isOptional,
        long timeoutMs,
        int maxRetries
) {}
