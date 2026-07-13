package com.sporekart.ai.approval.interfaces.rest.dto;

import java.util.Map;

public record ApprovalStatsDto(
    long totalRequests,
    long pending,
    long approved,
    long rejected,
    double avgReviewTimeMs,
    Map<String, Object> detailed
) {}
