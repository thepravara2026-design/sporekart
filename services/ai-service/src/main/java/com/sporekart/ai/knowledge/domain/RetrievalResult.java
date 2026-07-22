package com.sporekart.ai.knowledge.domain;

public record RetrievalResult(
    KnowledgeChunk chunk,
    Citation citation,
    double score,
    String retrievalStrategy
) {}
