package com.sporekart.ai.semantic.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "semantic_similarity_scores")
public class SemanticSimilarityScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "source_embedding_id", nullable = false)
    private SemanticEmbeddingEntity sourceEmbedding;

    @ManyToOne
    @JoinColumn(name = "target_embedding_id", nullable = false)
    private SemanticEmbeddingEntity targetEmbedding;

    @Column(nullable = false)
    private double similarity;

    @Column(nullable = false, length = 50)
    private String algorithm = "COSINE";

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public SemanticSimilarityScoreEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public SemanticEmbeddingEntity getSourceEmbedding() { return sourceEmbedding; }
    public void setSourceEmbedding(SemanticEmbeddingEntity sourceEmbedding) { this.sourceEmbedding = sourceEmbedding; }
    public SemanticEmbeddingEntity getTargetEmbedding() { return targetEmbedding; }
    public void setTargetEmbedding(SemanticEmbeddingEntity targetEmbedding) { this.targetEmbedding = targetEmbedding; }
    public double getSimilarity() { return similarity; }
    public void setSimilarity(double similarity) { this.similarity = similarity; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime deletedAt) { this.deletedAt = deletedAt; }
}
