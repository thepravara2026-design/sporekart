package com.sporekart.ai.knowledge.domain;

import java.util.List;

public record KnowledgeCitation(
        String id,
        String documentId,
        String documentTitle,
        String source,
        List<String> chunkIds,
        List<String> excerpts,
        Double relevanceScore,
        String retrievalContext) {
}
