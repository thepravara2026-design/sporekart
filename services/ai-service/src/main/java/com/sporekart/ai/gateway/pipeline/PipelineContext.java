package com.sporekart.ai.gateway.pipeline;

import com.sporekart.ai.gateway.contract.request.GatewayRequest;
import com.sporekart.ai.gateway.contract.response.GatewayResponse;
import com.sporekart.ai.gateway.domain.GatewayExecutionRequest;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record PipelineContext(
    String pipelineId,
    GatewayRequest request,
    GatewayResponse response,
    GatewayExecutionRequest executionContext,
    PipelineStage currentStage,
    Map<String, Object> attributes,
    Instant startedAt,
    Instant completedAt,
    boolean failed,
    String failureReason
) {
    public PipelineContext(GatewayRequest request, GatewayExecutionRequest executionContext) {
        this(UUID.randomUUID().toString(), request, null, executionContext,
            PipelineStage.VALIDATION, new LinkedHashMap<>(), Instant.now(), null, false, null);
    }

    public Optional<Object> getAttribute(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public PipelineContext advanceTo(PipelineStage stage) {
        return new PipelineContext(pipelineId, request, response, executionContext,
            stage, attributes, startedAt, completedAt, failed, failureReason);
    }

    public PipelineContext withResponse(GatewayResponse response) {
        return new PipelineContext(pipelineId, request, response, executionContext,
            currentStage, attributes, startedAt, Instant.now(), failed, failureReason);
    }

    public PipelineContext failed(String reason) {
        return new PipelineContext(pipelineId, request, response, executionContext,
            currentStage, attributes, startedAt, Instant.now(), true, reason);
    }
}
