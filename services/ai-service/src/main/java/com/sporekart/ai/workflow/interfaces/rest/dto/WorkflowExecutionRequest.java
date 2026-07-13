package com.sporekart.ai.workflow.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record WorkflowExecutionRequest(
        @NotNull UUID workflowId,
        String triggerType,
        Map<String, Object> inputData,
        UUID startedBy
) {}
