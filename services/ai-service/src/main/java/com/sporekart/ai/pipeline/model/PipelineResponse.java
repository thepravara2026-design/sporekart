package com.sporekart.ai.pipeline.model;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public record PipelineResponse(
        String responseId,
        String requestId,
        String provider,
        String model,
        String generatedOutput,
        String structuredOutput,
        TokenUsage tokenUsage,
        Duration latency,
        FinishReason finishReason,
        double cost,
        List<String> warnings,
        List<String> errors,
        Map<String, Object> metadata,
        String auditReference,
        boolean success,
        Instant completedAt) {

    public PipelineResponse {
        warnings = warnings == null ? List.of() : List.copyOf(warnings);
        errors = errors == null ? List.of() : List.copyOf(errors);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        tokenUsage = tokenUsage == null ? TokenUsage.EMPTY : tokenUsage;
        latency = latency == null ? Duration.ZERO : latency;
        completedAt = completedAt == null ? Instant.now() : completedAt;
        finishReason = finishReason == null ? (success ? FinishReason.STOP : FinishReason.ERROR) : finishReason;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PipelineResponse success(String responseId, String requestId, String provider,
            String model, String generatedOutput, TokenUsage tokenUsage, Duration latency) {
        return new PipelineResponse(responseId, requestId, provider, model, generatedOutput,
                null, tokenUsage, latency, FinishReason.STOP, 0.0, List.of(), List.of(),
                Map.of(), null, true, Instant.now());
    }

    public static PipelineResponse failure(String requestId, String error, String provider) {
        return new PipelineResponse(UUID.randomUUID().toString(), requestId, provider, null,
                null, null, TokenUsage.EMPTY, Duration.ZERO, FinishReason.ERROR, 0.0,
                List.of(), List.of(error), Map.of(), null, false, Instant.now());
    }

    public static class Builder {
        private String responseId;
        private String requestId;
        private String provider;
        private String model;
        private String generatedOutput;
        private String structuredOutput;
        private TokenUsage tokenUsage;
        private Duration latency;
        private FinishReason finishReason;
        private double cost;
        private List<String> warnings;
        private List<String> errors;
        private Map<String, Object> metadata;
        private String auditReference;
        private boolean success;
        private Instant completedAt;

        public Builder responseId(String val) { this.responseId = val; return this; }
        public Builder requestId(String val) { this.requestId = val; return this; }
        public Builder provider(String val) { this.provider = val; return this; }
        public Builder model(String val) { this.model = val; return this; }
        public Builder generatedOutput(String val) { this.generatedOutput = val; return this; }
        public Builder structuredOutput(String val) { this.structuredOutput = val; return this; }
        public Builder tokenUsage(TokenUsage val) { this.tokenUsage = val; return this; }
        public Builder latency(Duration val) { this.latency = val; return this; }
        public Builder finishReason(FinishReason val) { this.finishReason = val; return this; }
        public Builder cost(double val) { this.cost = val; return this; }
        public Builder warnings(List<String> val) { this.warnings = val; return this; }
        public Builder errors(List<String> val) { this.errors = val; return this; }
        public Builder metadata(Map<String, Object> val) { this.metadata = val; return this; }
        public Builder auditReference(String val) { this.auditReference = val; return this; }
        public Builder success(boolean val) { this.success = val; return this; }
        public Builder completedAt(Instant val) { this.completedAt = val; return this; }
        public Builder addWarning(String warning) {
            if (this.warnings == null) this.warnings = new ArrayList<>();
            this.warnings.add(warning);
            return this;
        }
        public Builder addError(String error) {
            if (this.errors == null) this.errors = new ArrayList<>();
            this.errors.add(error);
            return this;
        }
        public Builder addMetadata(String key, Object value) {
            if (this.metadata == null) this.metadata = new HashMap<>();
            this.metadata.put(key, value);
            return this;
        }

        public PipelineResponse build() {
            return new PipelineResponse(responseId, requestId, provider, model, generatedOutput,
                    structuredOutput, tokenUsage, latency, finishReason, cost, warnings, errors,
                    metadata, auditReference, success, completedAt);
        }
    }
}
