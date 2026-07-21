package com.sporekart.ai.gateway.contract.request;

import java.util.List;
import java.util.Map;

public record VisionRequest(
    String model,
    String prompt,
    List<Map<String, Object>> images,
    Double temperature,
    Integer maxTokens
) {}
