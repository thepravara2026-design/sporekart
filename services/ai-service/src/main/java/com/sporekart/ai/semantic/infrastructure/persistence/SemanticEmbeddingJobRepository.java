package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticEmbeddingJobRepository extends JpaRepository<SemanticEmbeddingJobEntity, UUID> {
    List<SemanticEmbeddingJobEntity> findByIsDeletedFalse();
    Optional<SemanticEmbeddingJobEntity> findByIdAndIsDeletedFalse(UUID id);
    List<SemanticEmbeddingJobEntity> findByTypeAndIsDeletedFalse(String type);
    List<SemanticEmbeddingJobEntity> findByStatusAndIsDeletedFalse(String status);
    List<SemanticEmbeddingJobEntity> findByCreatedByAndIsDeletedFalse(UUID createdBy);
}
