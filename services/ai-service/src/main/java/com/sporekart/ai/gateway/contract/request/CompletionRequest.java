package com.sporekart.ai.gateway.contract.request;

import java.util.Map;

public record CompletionRequest(
    String model,
    String prompt,
    Double temperature,
    Integer maxTokens,
    Double topP,
    Double frequencyPenalty,
    Double presencePenalty,
    Map<String, Object> stop,
    String userId
) {}
