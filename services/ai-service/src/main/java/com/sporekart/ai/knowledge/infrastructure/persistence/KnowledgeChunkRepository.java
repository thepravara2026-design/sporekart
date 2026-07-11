package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KnowledgeChunkRepository extends JpaRepository<KnowledgeChunkEntity, UUID> {
    List<KnowledgeChunkEntity> findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(UUID documentId);
    List<KnowledgeChunkEntity> findByDocumentIdAndChunkIndexGreaterThanEqualAndIsDeletedFalse(UUID documentId, int fromIndex);
    Optional<KnowledgeChunkEntity> findByDocumentIdAndChunkIndexAndIsDeletedFalse(UUID documentId, int chunkIndex);
    List<KnowledgeChunkEntity> findByDocumentIdAndIsActiveTrueAndIsDeletedFalseOrderByChunkIndex(UUID documentId);
    void deleteByDocumentId(UUID documentId);
}
