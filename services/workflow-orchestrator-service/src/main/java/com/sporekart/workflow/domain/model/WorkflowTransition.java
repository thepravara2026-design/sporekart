package com.sporekart.workflow.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record WorkflowTransition(
    String id,
    WorkflowState from,
    WorkflowState to,
    String action,
    String triggeredBy,
    Map<String, Object> details,
    Instant timestamp
) {
    public static WorkflowTransition create(
        WorkflowState from,
        WorkflowState to,
        String action,
        String triggeredBy,
        Map<String, Object> details
    ) {
        return new WorkflowTransition(
            UUID.randomUUID().toString(),
            from, to, action, triggeredBy,
            details == null ? Map.of() : Map.copyOf(details),
            Instant.now()
        );
    }
}
