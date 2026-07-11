package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KnowledgeDocumentVersionRepository extends JpaRepository<KnowledgeDocumentVersionEntity, UUID> {
    List<KnowledgeDocumentVersionEntity> findByDocumentIdAndIsDeletedFalseOrderByVersionNumberDesc(UUID documentId);
    Optional<KnowledgeDocumentVersionEntity> findByDocumentIdAndVersionNumberAndIsDeletedFalse(UUID documentId, int versionNumber);
    Optional<KnowledgeDocumentVersionEntity> findByIdAndIsDeletedFalse(UUID id);
    int countByDocumentIdAndIsDeletedFalse(UUID documentId);
}
