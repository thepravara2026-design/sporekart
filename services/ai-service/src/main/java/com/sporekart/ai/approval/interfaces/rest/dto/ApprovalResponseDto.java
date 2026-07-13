package com.sporekart.ai.approval.interfaces.rest.dto;

public record ApprovalResponseDto(
    String id,
    String module,
    String action,
    String status,
    String userId,
    String reason,
    String urgency,
    long createdAt
) {}
