package com.sporekart.ai.policy.interfaces.rest.dto;

import java.util.Map;

public record ViolationDto(String ruleName, String message, String severity, Map<String, Object> details, boolean overridable) {}
