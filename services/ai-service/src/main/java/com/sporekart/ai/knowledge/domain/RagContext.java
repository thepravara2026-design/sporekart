package com.sporekart.ai.knowledge.domain;

import java.util.List;

public record RagContext(
    String query,
    List<RetrievalResult> results,
    String assembledContext,
    int totalTokens,
    int maxTokens,
    List<String> sources
) {}
