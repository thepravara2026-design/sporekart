package com.sporekart.ai.providers.contracts;

public record StreamingContract(
    String model,
    String prompt,
    boolean stream,
    Double temperature,
    Integer maxTokens
) {}
