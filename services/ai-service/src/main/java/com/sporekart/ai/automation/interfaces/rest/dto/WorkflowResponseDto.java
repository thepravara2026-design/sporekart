package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record WorkflowResponseDto(
    String id,
    String workflowName,
    String status,
    Map<String, Object> result,
    String startedAt,
    String completedAt
) {}
