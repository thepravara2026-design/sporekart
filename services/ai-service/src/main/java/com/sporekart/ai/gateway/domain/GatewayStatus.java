package com.sporekart.ai.gateway.domain;

import java.util.Map;

public record GatewayStatus(
        boolean gatewayEnabled,
        long totalRequests,
        long successfulRequests,
        long failedRequests,
        double averageLatencyMs,
        Map<String, Object> moduleStatus,
        Map<String, Boolean> featureFlags) {
}
