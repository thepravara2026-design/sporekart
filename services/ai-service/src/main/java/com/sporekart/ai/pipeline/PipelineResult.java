package com.sporekart.ai.pipeline;

import com.sporekart.ai.pipeline.model.PipelineResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public record PipelineResult(
        boolean success,
        PipelineResponse response,
        String pipelineId,
        Duration totalDuration,
        List<String> warnings,
        List<String> errors,
        int retryCount,
        String selectedProvider,
        String selectedModel,
        Instant executedAt) {

    public static PipelineResult success(String pipelineId, PipelineResponse response,
            Duration duration, String provider, String model) {
        return new PipelineResult(true, response, pipelineId, duration,
                response.warnings(), List.of(), 0, provider, model, Instant.now());
    }

    public static PipelineResult failure(String pipelineId, String requestId, String error,
            Duration duration, int retryCount) {
        return new PipelineResult(false, PipelineResponse.failure(requestId, error, null),
                pipelineId, duration, List.of(), List.of(error), retryCount, null, null, Instant.now());
    }
}
