package com.sporekart.ai.pipeline.model;

import java.time.Instant;
import java.util.*;

public record PipelineRequest(
        String requestId,
        String tenantId,
        String userId,
        String workspace,
        String module,
        String conversationId,
        String promptId,
        String promptVersion,
        String providerPreference,
        String model,
        double temperature,
        int maxTokens,
        double topP,
        boolean stream,
        Map<String, Object> context,
        Map<String, Object> variables,
        Map<String, Object> metadata,
        Instant timestamp,
        String correlationId,
        RequestSource source) {

    public PipelineRequest {
        if (requestId == null || requestId.isBlank())
            throw new IllegalArgumentException("requestId must not be blank");
        if (tenantId == null || tenantId.isBlank())
            throw new IllegalArgumentException("tenantId must not be blank");
        context = context == null ? Map.of() : Map.copyOf(context);
        variables = variables == null ? Map.of() : Map.copyOf(variables);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        timestamp = timestamp == null ? Instant.now() : timestamp;
        source = source == null ? RequestSource.API : source;
        temperature = temperature <= 0 ? 0.7 : temperature;
        maxTokens = maxTokens <= 0 ? 2048 : maxTokens;
        topP = topP <= 0 ? 1.0 : topP;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String requestId;
        private String tenantId;
        private String userId;
        private String workspace;
        private String module;
        private String conversationId;
        private String promptId;
        private String promptVersion;
        private String providerPreference;
        private String model;
        private double temperature = 0.7;
        private int maxTokens = 2048;
        private double topP = 1.0;
        private boolean stream;
        private Map<String, Object> context;
        private Map<String, Object> variables;
        private Map<String, Object> metadata;
        private Instant timestamp;
        private String correlationId;
        private RequestSource source;

        public Builder requestId(String val) { this.requestId = val; return this; }
        public Builder tenantId(String val) { this.tenantId = val; return this; }
        public Builder userId(String val) { this.userId = val; return this; }
        public Builder workspace(String val) { this.workspace = val; return this; }
        public Builder module(String val) { this.module = val; return this; }
        public Builder conversationId(String val) { this.conversationId = val; return this; }
        public Builder promptId(String val) { this.promptId = val; return this; }
        public Builder promptVersion(String val) { this.promptVersion = val; return this; }
        public Builder providerPreference(String val) { this.providerPreference = val; return this; }
        public Builder model(String val) { this.model = val; return this; }
        public Builder temperature(double val) { this.temperature = val; return this; }
        public Builder maxTokens(int val) { this.maxTokens = val; return this; }
        public Builder topP(double val) { this.topP = val; return this; }
        public Builder stream(boolean val) { this.stream = val; return this; }
        public Builder context(Map<String, Object> val) { this.context = val; return this; }
        public Builder variables(Map<String, Object> val) { this.variables = val; return this; }
        public Builder metadata(Map<String, Object> val) { this.metadata = val; return this; }
        public Builder timestamp(Instant val) { this.timestamp = val; return this; }
        public Builder correlationId(String val) { this.correlationId = val; return this; }
        public Builder source(RequestSource val) { this.source = val; return this; }

        public PipelineRequest build() {
            return new PipelineRequest(requestId, tenantId, userId, workspace, module,
                    conversationId, promptId, promptVersion, providerPreference, model,
                    temperature, maxTokens, topP, stream, context, variables, metadata,
                    timestamp, correlationId, source);
        }
    }
}
