package com.sporekart.ai.knowledge.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_metadata", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"document_id", "meta_key"})
})
public class KnowledgeMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocumentEntity document;

    @Column(name = "meta_key", nullable = false, length = 255)
    private String metaKey;

    @Column(name = "meta_value", columnDefinition = "TEXT")
    private String metaValue;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public KnowledgeMetadataEntity() {}

    public KnowledgeMetadataEntity(KnowledgeDocumentEntity document, String metaKey, String metaValue) {
        this.document = document;
        this.metaKey = metaKey;
        this.metaValue = metaValue;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public KnowledgeDocumentEntity getDocument() { return document; }
    public void setDocument(KnowledgeDocumentEntity document) { this.document = document; }
    public String getMetaKey() { return metaKey; }
    public void setMetaKey(String metaKey) { this.metaKey = metaKey; }
    public String getMetaValue() { return metaValue; }
    public void setMetaValue(String metaValue) { this.metaValue = metaValue; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
