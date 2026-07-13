package com.sporekart.ai.workflow.domain;

import java.util.UUID;

public record WorkflowCondition(
    UUID id,
    UUID workflowId,
    UUID stepId,
    WorkflowConditionType type,
    String expression,
    String description
) {}
