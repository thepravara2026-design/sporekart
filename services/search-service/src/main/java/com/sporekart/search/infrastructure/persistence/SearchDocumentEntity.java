package com.sporekart.search.infrastructure.persistence;

import com.sporekart.search.domain.model.SearchDocument;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "search_documents")
public class SearchDocumentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 100)
    private String entityId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "indexed_at", nullable = false, updatable = false)
    private Instant indexedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SearchDocumentEntity() {}

    public SearchDocumentEntity(String id, String entityType, String entityId, String title, String description,
                                String content, String tags, String metadata, double score,
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

    public static SearchDocumentEntity fromDomain(SearchDocument doc) {
        String tagsStr = doc.getTags() != null ? String.join(",", doc.getTags()) : null;
        String metadataStr = doc.getMetadata() != null ? doc.getMetadata().toString() : null;
        return new SearchDocumentEntity(
            doc.getId(), doc.getEntityType(), doc.getEntityId(),
            doc.getTitle(), doc.getDescription(), doc.getContent(),
            tagsStr, metadataStr, doc.getScore(),
            doc.getIndexedAt(), doc.getUpdatedAt());
    }

    public SearchDocument toDomain() {
        List<String> tagList = tags != null && !tags.isEmpty()
            ? Arrays.asList(tags.split(",", -1))
            : List.of();
        Map<String, String> metadataMap = new HashMap<>();
        return new SearchDocument(id, entityType, entityId, title, description, content,
            tagList, metadataMap, score, indexedAt, updatedAt);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Instant getIndexedAt() { return indexedAt; }
    public void setIndexedAt(Instant indexedAt) { this.indexedAt = indexedAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
