package com.sporekart.ai.gateway.pipeline;

import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
import com.sporekart.ai.gateway.domain.GatewayExecutionRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PipelineExecutor {
    GatewayResponse execute(GatewayRequest request, GatewayExecutionRequest context);
    CompletableFuture<GatewayResponse> executeAsync(GatewayRequest request, GatewayExecutionRequest context);
    GatewayResponse executeWithStages(GatewayRequest request, GatewayExecutionRequest context, List<PipelineStage> stages);
    void registerInterceptor(PipelineInterceptor interceptor);
    void registerStageHandler(PipelineStage stage, StageHandler handler);
    boolean isStageEnabled(PipelineStage stage);
    PipelineContext getCurrentContext();

    interface StageHandler {
        void handle(PipelineContext context);
    }
}
