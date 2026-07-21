package com.sporekart.ai.providers.contracts;

import java.util.Map;

public record ImageGenerationContract(
    String model,
    String prompt,
    int n,
    String size,
    String quality,
    String style
) {}
