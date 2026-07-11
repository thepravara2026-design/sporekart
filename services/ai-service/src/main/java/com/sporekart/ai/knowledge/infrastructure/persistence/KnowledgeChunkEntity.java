package com.sporekart.ai.knowledge.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "knowledge_chunks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"document_id", "chunk_index"})
})
public class KnowledgeChunkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private KnowledgeDocumentEntity document;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private KnowledgeDocumentVersionEntity version;

    @Column(name = "chunk_index", nullable = false)
    private int chunkIndex;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "token_count")
    private int tokenCount;

    @Column(name = "char_count")
    private int charCount;

    @Column(name = "chunk_size_strategy", length = 50)
    private String chunkSizeStrategy = "FIXED";

    @ManyToOne
    @JoinColumn(name = "parent_chunk_id")
    private KnowledgeChunkEntity parentChunk;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public KnowledgeChunkEntity() {}

    public KnowledgeChunkEntity(KnowledgeDocumentEntity document, int chunkIndex, String content) {
        this.document = document;
        this.chunkIndex = chunkIndex;
        this.content = content;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public KnowledgeDocumentEntity getDocument() { return document; }
    public void setDocument(KnowledgeDocumentEntity document) { this.document = document; }
    public KnowledgeDocumentVersionEntity getVersion() { return version; }
    public void setVersion(KnowledgeDocumentVersionEntity version) { this.version = version; }
    public int getChunkIndex() { return chunkIndex; }
    public void setChunkIndex(int chunkIndex) { this.chunkIndex = chunkIndex; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getTokenCount() { return tokenCount; }
    public void setTokenCount(int tokenCount) { this.tokenCount = tokenCount; }
    public int getCharCount() { return charCount; }
    public void setCharCount(int charCount) { this.charCount = charCount; }
    public String getChunkSizeStrategy() { return chunkSizeStrategy; }
    public void setChunkSizeStrategy(String chunkSizeStrategy) { this.chunkSizeStrategy = chunkSizeStrategy; }
    public KnowledgeChunkEntity getParentChunk() { return parentChunk; }
    public void setParentChunk(KnowledgeChunkEntity parentChunk) { this.parentChunk = parentChunk; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
}
