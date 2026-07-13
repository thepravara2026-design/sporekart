package com.sporekart.ai.workflow.interfaces.rest.dto;

import com.sporekart.ai.workflow.domain.WorkflowSchedule;

import java.time.OffsetDateTime;
import java.util.UUID;

public record WorkflowScheduleResponse(
        UUID id,
        UUID workflowId,
        String cronExpression,
        boolean isActive,
        String timezone,
        OffsetDateTime lastExecutedAt,
        OffsetDateTime nextExecutionAt
) {
    public static WorkflowScheduleResponse from(WorkflowSchedule schedule) {
        return new WorkflowScheduleResponse(
                schedule.id(), schedule.workflowId(), schedule.cronExpression(),
                schedule.isActive(), schedule.timezone(),
                schedule.lastExecutedAt(), schedule.nextExecutionAt()
        );
    }
}
