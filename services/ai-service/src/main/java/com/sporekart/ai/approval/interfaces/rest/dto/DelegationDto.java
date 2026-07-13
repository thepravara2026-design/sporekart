package com.sporekart.ai.approval.interfaces.rest.dto;

public record DelegationDto(
    String id,
    String requestId,
    String fromReviewerId,
    String toReviewerId,
    String reason,
    boolean active
) {}
