package com.sporekart.ai.approval.interfaces.rest.dto;

public record EscalateDto(
    String reviewerId,
    String reason,
    String details
) {}
