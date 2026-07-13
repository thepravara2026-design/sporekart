package com.sporekart.ai.compliance.interfaces.rest.dto;

import java.util.Map;

public record ViolationDto(
    String id,
    String ruleId,
    String module,
    String severity,
    String description,
    Map<String, Object> details,
    boolean remediated,
    String detectedAt
) {}
