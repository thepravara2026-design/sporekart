package com.sporekart.ai.core.api;

public interface ModerationProvider {
    ModerationResult moderate(String text, String model);
    boolean isSafe(String text, String model);

    record ModerationResult(
            boolean flagged,
            String category,
            double confidenceScore,
            String recommendation) {
    }
}
