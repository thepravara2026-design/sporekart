package com.sporekart.ai.gateway.contract.request;

public record StreamingRequest(
    String model,
    String prompt,
    Boolean stream,
    Double temperature,
    Integer maxTokens
) {}
