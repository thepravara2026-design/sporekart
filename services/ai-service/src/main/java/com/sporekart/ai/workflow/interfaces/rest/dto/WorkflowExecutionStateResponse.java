package com.sporekart.ai.workflow.interfaces.rest.dto;

import com.sporekart.ai.workflow.domain.WorkflowExecutionState;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowExecutionStateResponse(
        UUID id,
        UUID executionId,
        UUID workflowId,
        String currentStep,
        String status,
        OffsetDateTime updatedAt
) {
    public static WorkflowExecutionStateResponse from(WorkflowExecutionState state) {
        return new WorkflowExecutionStateResponse(
                state.id(), state.executionId(), state.workflowId(),
                state.currentStep(), state.status(),
                state.updatedAt()
        );
    }
}
