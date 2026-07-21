package com.sporekart.ai.providers.contracts;

public record CompletionContract(
    String model,
    String prompt,
    Double temperature,
    Integer maxTokens,
    Double topP
) {}
