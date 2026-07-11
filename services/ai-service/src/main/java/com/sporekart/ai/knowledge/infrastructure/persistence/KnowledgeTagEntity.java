package com.sporekart.ai.knowledge.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"document_id", "tag"})
})
public class KnowledgeTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocumentEntity document;

    @Column(nullable = false, length = 100)
    private String tag;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public KnowledgeTagEntity() {}

    public KnowledgeTagEntity(KnowledgeDocumentEntity document, String tag) {
        this.document = document;
        this.tag = tag;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public KnowledgeDocumentEntity getDocument() { return document; }
    public void setDocument(KnowledgeDocumentEntity document) { this.document = document; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
