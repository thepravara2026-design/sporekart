package com.sporekart.ai.workflow.domain;

import java.util.UUID;

public record WorkflowAction(
    UUID id,
    UUID workflowId,
    UUID stepId,
    String actionType,
    String config,
    String onSuccess,
    String onFailure
) {}
