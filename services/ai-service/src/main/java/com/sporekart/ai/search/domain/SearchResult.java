package com.sporekart.ai.search.domain;

import java.util.Map;

public record SearchResult(
        String id,
        String title,
        String snippet,
        double score,
        String source,
        String category,
        Map<String, Object> metadata) {
}
