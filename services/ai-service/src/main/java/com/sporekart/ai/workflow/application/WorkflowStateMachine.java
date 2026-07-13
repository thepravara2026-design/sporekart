package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.domain.WorkflowExecutionStatus;
import java.util.Set;
import java.util.Map;

public class WorkflowStateMachine {
    private static final Map<WorkflowExecutionStatus, Set<WorkflowExecutionStatus>> TRANSITIONS = Map.of(
        WorkflowExecutionStatus.PENDING, Set.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.CANCELLED),
        WorkflowExecutionStatus.RUNNING, Set.of(WorkflowExecutionStatus.PAUSED, WorkflowExecutionStatus.COMPLETED, WorkflowExecutionStatus.FAILED, WorkflowExecutionStatus.CANCELLED, WorkflowExecutionStatus.TIMEOUT),
        WorkflowExecutionStatus.PAUSED, Set.of(WorkflowExecutionStatus.RUNNING, WorkflowExecutionStatus.CANCELLED),
        WorkflowExecutionStatus.COMPLETED, Set.of(),
        WorkflowExecutionStatus.FAILED, Set.of(WorkflowExecutionStatus.PENDING),
        WorkflowExecutionStatus.CANCELLED, Set.of(),
        WorkflowExecutionStatus.TIMEOUT, Set.of(WorkflowExecutionStatus.PENDING)
    );

    public static boolean canTransition(WorkflowExecutionStatus from, WorkflowExecutionStatus to) {
        return TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }

    public static WorkflowExecutionStatus transition(WorkflowExecutionStatus from, WorkflowExecutionStatus to) {
        if (!canTransition(from, to)) {
            throw new IllegalStateException("Cannot transition from " + from + " to " + to);
        }
        return to;
    }
}
