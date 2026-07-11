package com.sporekart.ai.search.domain;

import java.util.Map;

public record SearchQuery(
        String text,
        String category,
        String indexName,
        int maxResults,
        float minScore,
        Map<String, Object> filters) {
    public SearchQuery(String text) {
        this(text, null, null, 10, 0.0f, Map.of());
    }

    public SearchQuery(String text, String category) {
        this(text, category, null, 10, 0.0f, Map.of());
    }
}
