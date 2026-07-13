package com.sporekart.ai.compliance.interfaces.rest.dto;

public record ExceptionResponse(
    String id,
    String ruleId,
    String reason,
    String justification,
    String status,
    String expiresAt
) {}
