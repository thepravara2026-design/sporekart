package com.sporekart.ai.providers.contracts;

import java.util.List;
import java.util.Map;

public record VisionContract(
    String model,
    String prompt,
    List<Map<String, Object>> images,
    Double temperature,
    Integer maxTokens
) {}
