package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ApprovalWorkflow(
    UUID id,
    String name,
    String description,
    String module,
    List<ApprovalStatus> allowedTransitions,
    int maxLevels,
    boolean parallelEnabled,
    boolean sequentialEnabled,
    AssignmentStrategy strategy,
    int slaMinutes,
    Map<String, Object> config,
    boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
