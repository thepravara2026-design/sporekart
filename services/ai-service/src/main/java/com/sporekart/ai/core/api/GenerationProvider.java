package com.sporekart.ai.core.api;

public interface GenerationProvider {
    String generate(String prompt, String model);
    String generateWithConfig(String prompt, String model, GenerationConfig config);

    record GenerationConfig(
            double temperature,
            int maxTokens,
            double topP,
            int topK,
            double presencePenalty,
            double frequencyPenalty) {
        public GenerationConfig() {
            this(0.7, 2048, 0.95, 40, 0.0, 0.0);
        }
    }
}
