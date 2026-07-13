package com.sporekart.ai.workflow.domain;

import java.util.Map;
import java.util.UUID;

public record WorkflowStep(
    UUID id,
    UUID workflowId,
    String name,
    WorkflowStepType type,
    int orderIndex,
    String config,
    Map<String, String> metadata,
    boolean isOptional,
    long timeoutMs,
    int maxRetries
) {}
