package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticSimilarityScoreRepository extends JpaRepository<SemanticSimilarityScoreEntity, UUID> {
    List<SemanticSimilarityScoreEntity> findByIsDeletedFalse();
    Optional<SemanticSimilarityScoreEntity> findByIdAndIsDeletedFalse(UUID id);
    List<SemanticSimilarityScoreEntity> findBySourceEmbeddingIdAndIsDeletedFalse(UUID sourceEmbeddingId);
    List<SemanticSimilarityScoreEntity> findByTargetEmbeddingIdAndIsDeletedFalse(UUID targetEmbeddingId);
    List<SemanticSimilarityScoreEntity> findByAlgorithmAndIsDeletedFalse(String algorithm);
    List<SemanticSimilarityScoreEntity> findBySourceEmbeddingIdAndAlgorithmAndIsDeletedFalse(
            UUID sourceEmbeddingId, String algorithm);
}
