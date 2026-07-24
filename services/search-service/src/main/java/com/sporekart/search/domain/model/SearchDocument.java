package com.sporekart.search.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SearchDocument {
    private final String id;
    private final String entityType;
    private final String entityId;
    private final String title;
    private final String description;
    private final String content;
    private final List<String> tags;
    private final Map<String, String> metadata;
    private double score;
    private final Instant indexedAt;
    private final Instant updatedAt;

    public SearchDocument(String id, String entityType, String entityId, String title, String description,
            String content, List<String> tags, Map<String, String> metadata, double score,
            Instant indexedAt, Instant updatedAt) {
        this.id = id;
        this.entityType = entityType;
        this.entityId = entityId;
        this.title = title;
        this.description = description;
        this.content = content;
        this.tags = tags;
        this.metadata = metadata;
        this.score = score;
        this.indexedAt = indexedAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public List<String> getTags() {
        return tags;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Instant getIndexedAt() {
        return indexedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}