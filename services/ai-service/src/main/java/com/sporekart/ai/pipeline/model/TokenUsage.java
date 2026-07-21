package com.sporekart.ai.pipeline.model;

public record TokenUsage(
        int promptTokens,
        int completionTokens,
        int totalTokens) {

    public static final TokenUsage EMPTY = new TokenUsage(0, 0, 0);

    public TokenUsage {
        if (promptTokens < 0) throw new IllegalArgumentException("promptTokens must not be negative");
        if (completionTokens < 0) throw new IllegalArgumentException("completionTokens must not be negative");
    }

    public static TokenUsage of(int promptTokens, int completionTokens) {
        return new TokenUsage(promptTokens, completionTokens, promptTokens + completionTokens);
    }

    public TokenUsage add(TokenUsage other) {
        return new TokenUsage(
                this.promptTokens + other.promptTokens,
                this.completionTokens + other.completionTokens,
                this.totalTokens + other.totalTokens);
    }
}
