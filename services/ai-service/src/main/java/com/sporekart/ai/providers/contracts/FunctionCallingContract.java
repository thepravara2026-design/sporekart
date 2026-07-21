package com.sporekart.ai.providers.contracts;

import java.util.Map;

public record FunctionCallingContract(
    String model,
    String prompt,
    Map<String, Object> functionDefinition,
    boolean strict
) {}
