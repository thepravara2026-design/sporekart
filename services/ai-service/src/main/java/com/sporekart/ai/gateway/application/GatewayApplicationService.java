package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.core.domain.CorrelationId;
import com.sporekart.ai.gateway.api.AiGateway;
import com.sporekart.ai.gateway.domain.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GatewayApplicationService implements AiGateway {
    private final GatewayPipeline pipeline;
    private final GatewayDomainService domainService;
    private final GatewayResponseBuilder responseBuilder;
    private final GatewayExceptionTranslator exceptionTranslator;

    public GatewayApplicationService(GatewayPipeline pipeline, GatewayDomainService domainService,
            GatewayResponseBuilder responseBuilder,
            GatewayExceptionTranslator exceptionTranslator) {
        this.pipeline = pipeline;
        this.domainService = domainService;
        this.responseBuilder = responseBuilder;
        this.exceptionTranslator = exceptionTranslator;
    }

    @Override
    public AiResponse route(AiRequest request) {
        CorrelationId correlationId = CorrelationId.generate();
        try {
            return pipeline.execute(request, correlationId);
        } catch (Exception e) {
            return exceptionTranslator.translate(e);
        }
    }

    @Override
    public boolean supports(String providerType) {
        return true;
    }

    public AIExecutionResponse executeRequest(AIExecutionRequest request) {
        AIRequestMetadata metadata = new AIRequestMetadata("api", null, null, "v1", null);
        return domainService.execute(request, metadata);
    }

    public AIExecutionResponse validateRequest(AIExecutionRequest request) {
        if (request.prompt() == null || request.prompt().isBlank()) {
            String correlationId = java.util.UUID.randomUUID().toString();
            return AIExecutionResponse.failure(
                    java.util.UUID.randomUUID().toString(), correlationId,
                    "AI-006", "Prompt must not be blank", 0);
        }
        return AIExecutionResponse.success(
                java.util.UUID.randomUUID().toString(),
                java.util.UUID.randomUUID().toString(),
                "Request is valid", "none", "none", 0);
    }

    public AIHealthResponse getHealth() {
        return new AIHealthResponse("UP", true, false,
                Map.of("gateway", com.sporekart.ai.core.domain.AiModuleStatus.ACTIVE),
                "AI Gateway is operational");
    }

    public GatewayStatus getStatus() {
        return domainService.getStatus();
    }

    public Map<String, Boolean> getFeatures() {
        return responseBuilder.buildFeatureFlags();
    }
}
