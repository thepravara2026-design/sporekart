package com.sporekart.ai.providers.contracts;

public record JSONModeContract(
    String model,
    String prompt,
    String jsonSchema,
    boolean strict
) {}
