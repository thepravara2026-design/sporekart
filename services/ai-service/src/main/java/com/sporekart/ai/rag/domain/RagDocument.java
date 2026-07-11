package com.sporekart.ai.rag.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record RagDocument(
        String id,
        String title,
        String category,
        String content,
        String source,
        String contentType,
        Map<String, Object> metadata,
        OffsetDateTime indexedAt) {
}
