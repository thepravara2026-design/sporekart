package com.sporekart.ai.workflow.api;

import com.sporekart.ai.workflow.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkflowSchedulerService {
    WorkflowSchedule scheduleWorkflow(UUID workflowId, String cronExpression, String timezone);
    WorkflowSchedule createSchedule(UUID workflowId, String cronExpression, OffsetDateTime startAt, OffsetDateTime endAt, String timezone);
    void unscheduleWorkflow(UUID scheduleId);
    Optional<WorkflowSchedule> getSchedule(UUID workflowId);
    List<WorkflowSchedule> listSchedules(UUID workflowId);
    WorkflowSchedule pauseSchedule(UUID scheduleId);
    WorkflowSchedule resumeSchedule(UUID scheduleId);
    void processScheduledExecutions();
}
