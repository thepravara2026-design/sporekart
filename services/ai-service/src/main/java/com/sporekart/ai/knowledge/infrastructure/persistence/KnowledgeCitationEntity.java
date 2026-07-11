package com.sporekart.ai.knowledge.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_citations")
public class KnowledgeCitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocumentEntity document;

    @Column(name = "retrieval_request_id")
    private UUID retrievalRequestId;

    @Column(name = "chunk_ids", columnDefinition = "TEXT[]")
    private String[] chunkIds;

    @Column(columnDefinition = "TEXT[]")
    private String[] excerpts;

    @Column(name = "relevance_score")
    private Double relevanceScore;

    @Column(name = "retrieval_context", columnDefinition = "TEXT")
    private String retrievalContext;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    public KnowledgeCitationEntity() {}

    public KnowledgeCitationEntity(KnowledgeDocumentEntity document) {
        this.document = document;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public KnowledgeDocumentEntity getDocument() { return document; }
    public void setDocument(KnowledgeDocumentEntity document) { this.document = document; }
    public UUID getRetrievalRequestId() { return retrievalRequestId; }
    public void setRetrievalRequestId(UUID retrievalRequestId) { this.retrievalRequestId = retrievalRequestId; }
    public String[] getChunkIds() { return chunkIds; }
    public void setChunkIds(String[] chunkIds) { this.chunkIds = chunkIds; }
    public String[] getExcerpts() { return excerpts; }
    public void setExcerpts(String[] excerpts) { this.excerpts = excerpts; }
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
    public String getRetrievalContext() { return retrievalContext; }
    public void setRetrievalContext(String retrievalContext) { this.retrievalContext = retrievalContext; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
}
