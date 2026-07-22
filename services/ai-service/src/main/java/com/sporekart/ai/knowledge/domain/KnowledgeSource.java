package com.sporekart.ai.knowledge.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class KnowledgeSource {
    private final KnowledgeSourceId id;
    private final String name;
    private final String description;
    private final String workspaceId;
    private final String owner;
    private final DocumentType sourceType;
    private final String sourceUrl;
    private final String version;
    private DocumentStatus status;
    private final String language;
    private final List<String> tags;
    private final Map<String, String> metadata;
    private final int retentionDays;
    private final Instant createdAt;
    private Instant updatedAt;

    public KnowledgeSource(KnowledgeSourceId id, String name, String description, String workspaceId,
                           String owner, DocumentType sourceType, String sourceUrl, String version,
                           DocumentStatus status, String language, List<String> tags,
                           Map<String, String> metadata, int retentionDays,
                           Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.workspaceId = workspaceId;
        this.owner = owner;
        this.sourceType = sourceType;
        this.sourceUrl = sourceUrl;
        this.version = version;
        this.status = status;
        this.language = language;
        this.tags = tags != null ? List.copyOf(tags) : List.of();
        this.metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        this.retentionDays = retentionDays;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public KnowledgeSourceId id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public String workspaceId() { return workspaceId; }
    public String owner() { return owner; }
    public DocumentType sourceType() { return sourceType; }
    public String sourceUrl() { return sourceUrl; }
    public String version() { return version; }
    public DocumentStatus status() { return status; }
    public String language() { return language; }
    public List<String> tags() { return tags; }
    public Map<String, String> metadata() { return metadata; }
    public int retentionDays() { return retentionDays; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public void archive() {
        this.status = DocumentStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    public void publish() {
        this.status = DocumentStatus.PUBLISHED;
        this.updatedAt = Instant.now();
    }

    public void markDeleted() {
        this.status = DocumentStatus.DELETED;
        this.updatedAt = Instant.now();
    }

    public void updateMetadata(Map<String, String> newMetadata) {
        if (newMetadata != null) {
            this.metadata.clear();
            this.metadata.putAll(newMetadata);
        }
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeSource that = (KnowledgeSource) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KnowledgeSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
