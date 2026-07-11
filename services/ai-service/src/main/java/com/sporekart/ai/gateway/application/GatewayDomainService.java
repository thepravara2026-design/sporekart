package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.application.exception.FeatureDisabledException;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.CorrelationId;
import com.sporekart.ai.gateway.domain.AIExecutionRequest;
import com.sporekart.ai.gateway.domain.AIExecutionResponse;
import com.sporekart.ai.gateway.domain.AIRequestMetadata;
import com.sporekart.ai.gateway.domain.GatewayStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GatewayDomainService {
    private final FeatureFlagService featureFlagService;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong successfulRequests = new AtomicLong(0);
    private final AtomicLong failedRequests = new AtomicLong(0);

    public GatewayDomainService(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public AIExecutionResponse execute(AIExecutionRequest request, AIRequestMetadata metadata) {
        String requestId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        totalRequests.incrementAndGet();

        try {
            if (!featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)) {
                failedRequests.incrementAndGet();
                return AIExecutionResponse.failure(requestId, correlationId,
                        "AI-001", "AI platform is disabled", elapsedMs(startTime));
            }

            if (request.prompt() == null || request.prompt().isBlank()) {
                failedRequests.incrementAndGet();
                return AIExecutionResponse.failure(requestId, correlationId,
                        "AI-006", "Prompt must not be blank", elapsedMs(startTime));
            }

            if (request.prompt().length() > 32000) {
                failedRequests.incrementAndGet();
                return AIExecutionResponse.failure(requestId, correlationId,
                        "AI-008", "Prompt exceeds maximum length", elapsedMs(startTime));
            }

            successfulRequests.incrementAndGet();
            return AIExecutionResponse.success(requestId, correlationId,
                    "Mock response: " + request.prompt(), "MOCK", "mock-model", elapsedMs(startTime));

        } catch (Exception e) {
            failedRequests.incrementAndGet();
            return AIExecutionResponse.failure(requestId, correlationId,
                    "AI-999", "Internal error: " + e.getMessage(), elapsedMs(startTime));
        }
    }

    public GatewayStatus getStatus() {
        long total = totalRequests.get();
        long successful = successfulRequests.get();
        long failed = failedRequests.get();
        double avgLatency = total > 0 ? (double) successful / total * 100 : 0;

        Map<FeatureFlagName, Boolean> rawFlags = featureFlagService.getAllFlags();
        Map<String, Boolean> flags = new HashMap<>();
        for (Map.Entry<FeatureFlagName, Boolean> entry : rawFlags.entrySet()) {
            flags.put(entry.getKey().name(), entry.getValue());
        }
        Map<String, Object> moduleStatus = Map.of(
                "gateway", featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED),
                "platform", featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED));

        return new GatewayStatus(
                featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED),
                total, successful, failed, avgLatency, moduleStatus, flags);
    }

    private long elapsedMs(long startTime) {
        return System.currentTimeMillis() - startTime;
    }
}
