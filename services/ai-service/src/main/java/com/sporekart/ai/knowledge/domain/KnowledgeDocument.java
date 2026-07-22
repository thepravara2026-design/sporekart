package com.sporekart.ai.knowledge.domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class KnowledgeDocument {
    private final KnowledgeDocumentId id;
    private final KnowledgeSourceId sourceId;
    private final KnowledgeCollectionId collectionId;
    private final String title;
    private final DocumentType documentType;
    private String content;
    private final Map<String, String> metadata;
    private final String version;
    private DocumentStatus status;
    private final String checksum;
    private final long fileSize;
    private final String author;
    private final String language;
    private int chunkCount;
    private final Instant createdAt;
    private Instant updatedAt;

    public KnowledgeDocument(KnowledgeDocumentId id, KnowledgeSourceId sourceId,
                             KnowledgeCollectionId collectionId, String title,
                             DocumentType documentType, String content,
                             Map<String, String> metadata, String version,
                             DocumentStatus status, String checksum, long fileSize,
                             String author, String language, int chunkCount,
                             Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sourceId = sourceId;
        this.collectionId = collectionId;
        this.title = title;
        this.documentType = documentType;
        this.content = content;
        this.metadata = metadata != null ? Collections.unmodifiableMap(Map.copyOf(metadata)) : Map.of();
        this.version = version;
        this.status = status;
        this.checksum = checksum;
        this.fileSize = fileSize;
        this.author = author;
        this.language = language;
        this.chunkCount = chunkCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public KnowledgeDocumentId id() { return id; }
    public KnowledgeSourceId sourceId() { return sourceId; }
    public KnowledgeCollectionId collectionId() { return collectionId; }
    public String title() { return title; }
    public DocumentType documentType() { return documentType; }
    public String content() { return content; }
    public Map<String, String> metadata() { return metadata; }
    public String version() { return version; }
    public DocumentStatus status() { return status; }
    public String checksum() { return checksum; }
    public long fileSize() { return fileSize; }
    public String author() { return author; }
    public String language() { return language; }
    public int chunkCount() { return chunkCount; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    public void publish() {
        this.status = DocumentStatus.PUBLISHED;
        this.updatedAt = Instant.now();
    }

    public void archive() {
        this.status = DocumentStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    public void markDeleted() {
        this.status = DocumentStatus.DELETED;
        this.updatedAt = Instant.now();
    }

    public void setChunkCount(int chunkCount) {
        this.chunkCount = chunkCount;
        this.updatedAt = Instant.now();
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeDocument that = (KnowledgeDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KnowledgeDocument{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                '}';
    }
}
