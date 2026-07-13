package com.sporekart.ai.workflow.interfaces.rest.dto;

import com.sporekart.ai.workflow.domain.WorkflowStep;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowStepResponse(
        UUID id,
        UUID workflowId,
        String name,
        String stepType,
        int orderIndex,
        String config,
        boolean isOptional,
        long timeoutMs,
        int maxRetries
) {
    public static WorkflowStepResponse from(WorkflowStep step) {
        return new WorkflowStepResponse(
                step.id(), step.workflowId(), step.name(),
                step.type().name(), step.orderIndex(),
                step.config(), step.isOptional(),
                step.timeoutMs(), step.maxRetries()
        );
    }
}
