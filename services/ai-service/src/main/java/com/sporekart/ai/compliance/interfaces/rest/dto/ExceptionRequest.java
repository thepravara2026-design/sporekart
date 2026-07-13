package com.sporekart.ai.compliance.interfaces.rest.dto;

public record ExceptionRequest(
    String ruleId,
    String reason,
    String justification,
    String requestedBy
) {}
