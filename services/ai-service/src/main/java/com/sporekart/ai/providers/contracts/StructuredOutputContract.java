package com.sporekart.ai.providers.contracts;

import java.util.Map;

public record StructuredOutputContract(
    String model,
    String prompt,
    Map<String, Object> schema,
    String outputMode
) {}
