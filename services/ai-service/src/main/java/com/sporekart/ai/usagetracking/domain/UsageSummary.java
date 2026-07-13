package com.sporekart.ai.usagetracking.domain;

public record UsageSummary(
        String usageDate,
        String providerId,
        String modelId,
        int totalRequests,
        int totalSuccess,
        int totalFailure,
        long totalTokens,
        double avgExecutionTimeMs
) {
}
