package com.sporekart.ai.gateway.contract.response;

public record FineTuningResponse(
    String id,
    String model,
    String status,
    String trainedModel,
    String trainingFile,
    String validationFile,
    Usage usage
) {
    public record Usage(int totalTokens, int trainingTokens) {}
}
