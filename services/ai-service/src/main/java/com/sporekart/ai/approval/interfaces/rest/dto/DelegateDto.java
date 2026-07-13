package com.sporekart.ai.approval.interfaces.rest.dto;

public record DelegateDto(
    String fromReviewerId,
    String toReviewerId,
    String reason
) {}
