package com.sporekart.ai.prompt.domain.valueobject;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record PromptExecutionPolicy(
    Integer maxTokens,
    Double temperature,
    Double topP,
    List<String> stopSequences,
    int retryCount,
    Duration timeout,
    String fallbackPromptId,
    Set<String> fallbackProviders,
    Set<String> allowedProviders,
    boolean streamingEnabled,
    boolean structuredOutput,
    boolean jsonEnforcement,
    String safetyPolicy
) {
    public PromptExecutionPolicy {
        stopSequences = stopSequences == null ? List.of() : List.copyOf(stopSequences);
        fallbackProviders = fallbackProviders == null ? Set.of() : Set.copyOf(fallbackProviders);
        allowedProviders = allowedProviders == null ? Set.of() : Set.copyOf(allowedProviders);
        if (retryCount < 0) throw new IllegalArgumentException("Retry count must not be negative");
        if (timeout != null && timeout.isNegative()) throw new IllegalArgumentException("Timeout must not be negative");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static PromptExecutionPolicy defaults() {
        return new PromptExecutionPolicy(2048, 0.7, 1.0, List.of(), 0,
            Duration.ofSeconds(60), null, Set.of(), Set.of(), false, false, false, null);
    }

    public static class Builder {
        private Integer maxTokens;
        private Double temperature;
        private Double topP;
        private List<String> stopSequences;
        private int retryCount;
        private Duration timeout;
        private String fallbackPromptId;
        private Set<String> fallbackProviders;
        private Set<String> allowedProviders;
        private boolean streamingEnabled;
        private boolean structuredOutput;
        private boolean jsonEnforcement;
        private String safetyPolicy;

        public Builder maxTokens(Integer maxTokens) { this.maxTokens = maxTokens; return this; }
        public Builder temperature(Double temperature) { this.temperature = temperature; return this; }
        public Builder topP(Double topP) { this.topP = topP; return this; }
        public Builder stopSequences(List<String> stopSequences) { this.stopSequences = stopSequences; return this; }
        public Builder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public Builder timeout(Duration timeout) { this.timeout = timeout; return this; }
        public Builder fallbackPromptId(String fallbackPromptId) { this.fallbackPromptId = fallbackPromptId; return this; }
        public Builder fallbackProviders(Set<String> fallbackProviders) { this.fallbackProviders = fallbackProviders; return this; }
        public Builder allowedProviders(Set<String> allowedProviders) { this.allowedProviders = allowedProviders; return this; }
        public Builder streamingEnabled(boolean streamingEnabled) { this.streamingEnabled = streamingEnabled; return this; }
        public Builder structuredOutput(boolean structuredOutput) { this.structuredOutput = structuredOutput; return this; }
        public Builder jsonEnforcement(boolean jsonEnforcement) { this.jsonEnforcement = jsonEnforcement; return this; }
        public Builder safetyPolicy(String safetyPolicy) { this.safetyPolicy = safetyPolicy; return this; }

        public PromptExecutionPolicy build() {
            return new PromptExecutionPolicy(maxTokens, temperature, topP, stopSequences,
                retryCount, timeout, fallbackPromptId, fallbackProviders, allowedProviders,
                streamingEnabled, structuredOutput, jsonEnforcement, safetyPolicy);
        }
    }
}
