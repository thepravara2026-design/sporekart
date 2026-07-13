package com.sporekart.ai.semantic.domain;

import java.util.Map;

public record SemanticSearchResult(
        String documentId,
        String content,
        double score,
        int rank,
        Map<String, String> metadata) {
}
