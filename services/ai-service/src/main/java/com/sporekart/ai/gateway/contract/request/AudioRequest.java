package com.sporekart.ai.gateway.contract.request;

public record AudioRequest(
    String model,
    byte[] audioData,
    String format,
    String language,
    String userId
) {}
