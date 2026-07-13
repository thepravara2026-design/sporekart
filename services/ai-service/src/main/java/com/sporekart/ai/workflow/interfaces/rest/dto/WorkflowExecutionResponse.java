package com.sporekart.ai.workflow.interfaces.rest.dto;

import com.sporekart.ai.workflow.domain.WorkflowExecution;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowExecutionResponse(
        UUID id,
        UUID workflowId,
        String workflowVersion,
        String status,
        String triggerType,
        UUID startedBy,
        OffsetDateTime startedAt,
        OffsetDateTime completedAt,
        String errorMessage,
        int retryCount
) {
    public static WorkflowExecutionResponse from(WorkflowExecution exec) {
        return new WorkflowExecutionResponse(
                exec.id(), exec.workflowId(), exec.workflowVersion(),
                exec.status().name(), exec.triggerType(),
                exec.startedBy(), exec.startedAt(),
                exec.completedAt(), exec.errorMessage(),
                exec.retryCount()
        );
    }
}
