package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record WorkflowRequestDto(
    String workflowName,
    Map<String, Object> context
) {}
