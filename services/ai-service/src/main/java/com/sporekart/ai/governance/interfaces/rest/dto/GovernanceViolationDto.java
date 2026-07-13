package com.sporekart.ai.governance.interfaces.rest.dto;

import java.util.Map;

public record GovernanceViolationDto(
    String ruleName,
    String message,
    String severity,
    Map<String, Object> details,
    boolean overridable
) {}
