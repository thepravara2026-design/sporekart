package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorkflowSimulation(
    String id,
    String instanceId,
    String workflowName,
    WorkflowType type,
    boolean success,
    boolean approvalRequired,
    boolean approved,
    List<MockAction> actions,
    List<String> decisions,
    List<String> errors,
    Map<String, Object> result,
    Instant executedAt
) {
    public static WorkflowSimulation create(
        String instanceId,
        String workflowName,
        WorkflowType type,
        boolean success,
        boolean approvalRequired,
        boolean approved,
        List<MockAction> actions,
        List<String> decisions,
        List<String> errors,
        Map<String, Object> result
    ) {
        return new WorkflowSimulation(
            UUID.randomUUID().toString(),
            instanceId, workflowName, type, success, approvalRequired, approved,
            actions == null ? List.of() : List.copyOf(actions),
            decisions == null ? List.of() : List.copyOf(decisions),
            errors == null ? List.of() : List.copyOf(errors),
            result == null ? Map.of() : Map.copyOf(result),
            Instant.now()
        );
    }
}
