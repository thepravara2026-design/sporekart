package com.sporekart.ai.gateway.pipeline;

import com.sporekart.ai.gateway.contract.response.GatewayResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record PipelineResult(
    boolean success,
    GatewayResponse response,
    PipelineStage completedStage,
    PipelineStage failedStage,
    Duration totalDuration,
    List<String> warnings,
    List<String> errors,
    Instant executedAt
) {
    public static PipelineResult success(GatewayResponse response, PipelineStage stage, Duration duration) {
        return new PipelineResult(true, response, stage, null, duration, List.of(), List.of(), Instant.now());
    }

    public static PipelineResult failure(PipelineStage stage, String error) {
        return new PipelineResult(false, null, stage, stage, Duration.ZERO, List.of(), List.of(error), Instant.now());
    }
}
