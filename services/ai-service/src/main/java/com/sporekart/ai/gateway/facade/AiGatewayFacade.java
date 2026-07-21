package com.sporekart.ai.gateway.facade;

import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
import com.sporekart.ai.gateway.domain.GatewayExecutionRequest;
import com.sporekart.ai.gateway.pipeline.PipelineResult;

import java.util.concurrent.CompletableFuture;

public interface AiGatewayFacade {
    GatewayResponse execute(GatewayRequest request);
    GatewayResponse executeWithContext(GatewayRequest request, GatewayExecutionRequest context);
    CompletableFuture<GatewayResponse> executeAsync(GatewayRequest request);
    PipelineResult executeWithPipeline(GatewayRequest request, GatewayExecutionRequest context);
    HealthStatus healthCheck();

    interface HealthStatus {
        boolean isHealthy();
        java.util.Map<String, Object> details();
    }
}
