package com.sporekart.ai.semantic.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticEmbeddingRepository extends JpaRepository<SemanticEmbeddingEntity, UUID> {
    List<SemanticEmbeddingEntity> findByIsDeletedFalse();
    Optional<SemanticEmbeddingEntity> findByIdAndIsDeletedFalse(UUID id);
    Optional<SemanticEmbeddingEntity> findByContentAndIsDeletedFalse(String content);
    List<SemanticEmbeddingEntity> findByProviderAndIsDeletedFalse(String provider);
    List<SemanticEmbeddingEntity> findByModelAndIsDeletedFalse(String model);
    List<SemanticEmbeddingEntity> findByStatusAndIsDeletedFalse(String status);
    List<SemanticEmbeddingEntity> findByProviderAndModelAndIsDeletedFalse(String provider, String model);
}
