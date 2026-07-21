package com.sporekart.ai.gateway.contract.request;

import java.util.List;

public record EmbeddingRequest(
    String model,
    List<String> input,
    String userId
) {}
