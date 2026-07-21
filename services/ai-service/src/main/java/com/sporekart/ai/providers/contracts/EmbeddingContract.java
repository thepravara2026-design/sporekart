package com.sporekart.ai.providers.contracts;

import java.util.List;

public record EmbeddingContract(
    String model,
    List<String> input,
    String encodingFormat
) {}
