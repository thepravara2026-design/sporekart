package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.gateway.domain.AIExecutionResponse;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class GatewayResponseBuilder {

    private final FeatureFlagService featureFlagService;

    public GatewayResponseBuilder(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public <T> ResponseEnvelope<T> buildSuccess(T data) {
        return ResponseEnvelope.ok(data);
    }

    public <T> ResponseEnvelope<T> buildSuccess(T data, String correlationId) {
        return ResponseEnvelope.ok(data, correlationId);
    }

    public <T> ResponseEnvelope<T> buildError(String errorCode, String errorMessage) {
        return ResponseEnvelope.error(errorCode, errorMessage);
    }

    public <T> ResponseEnvelope<T> buildError(String errorCode, String errorMessage, String correlationId) {
        return new ResponseEnvelope<>(false, null, errorCode, errorMessage, null, correlationId, Map.of());
    }

    public ResponseEnvelope<AIExecutionResponse> fromExecutionResponse(AIExecutionResponse response) {
        if (response.isSuccess()) {
            return ResponseEnvelope.ok(response, response.correlationId());
        }
        return ResponseEnvelope.error(response.error().code(), response.error().message());
    }

    public Map<String, Boolean> buildFeatureFlags() {
        Map<String, Boolean> flags = new LinkedHashMap<>();
        flags.put("AI_PLATFORM_ENABLED", featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED));
        flags.put("AI_GATEWAY_ENABLED", featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED));
        flags.put("AI_REQUEST_LOGGING", featureFlagService.isEnabled(FeatureFlagName.AI_REQUEST_LOGGING));
        flags.put("AI_RATE_LIMITING", featureFlagService.isEnabled(FeatureFlagName.AI_RATE_LIMITING));
        flags.put("AI_METRICS", featureFlagService.isEnabled(FeatureFlagName.AI_METRICS));
        return flags;
    }
}
