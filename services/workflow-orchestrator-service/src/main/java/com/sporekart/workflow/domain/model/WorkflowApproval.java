package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowApproval(
    String id,
    String instanceId,
    String workflowName,
    String requestedBy,
    String assignedTo,
    String status,
    String reason,
    Map<String, Object> details,
    Instant requestedAt,
    Instant decidedAt
) {
    public static WorkflowApproval create(
        String instanceId,
        String workflowName,
        String requestedBy,
        String assignedTo,
        Map<String, Object> details
    ) {
        return new WorkflowApproval(
            UUID.randomUUID().toString(),
            instanceId, workflowName, requestedBy, assignedTo,
            "PENDING", null,
            details == null ? Map.of() : Map.copyOf(details),
            Instant.now(), null
        );
    }

    public WorkflowApproval withDecision(String status, String reason) {
        return new WorkflowApproval(id, instanceId, workflowName, requestedBy, assignedTo,
            status, reason, details, requestedAt, Instant.now());
    }
}
