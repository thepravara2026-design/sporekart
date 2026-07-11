package com.sporekart.ai.knowledge.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record KnowledgeDocument(
        String id,
        String title,
        String category,
        String language,
        String author,
        String source,
        String content,
        DocumentStatus status,
        DocumentVisibility visibility,
        String businessModule,
        String region,
        int version,
        List<String> tags,
        Map<String, String> metadata,
        String createdBy,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public KnowledgeDocument(String title, String category, String content) {
        this(null, title, category, "en", null, null, content, DocumentStatus.DRAFT,
                DocumentVisibility.INTERNAL, null, null, 1, List.of(), Map.of(),
                null, OffsetDateTime.now(), OffsetDateTime.now());
    }
}
