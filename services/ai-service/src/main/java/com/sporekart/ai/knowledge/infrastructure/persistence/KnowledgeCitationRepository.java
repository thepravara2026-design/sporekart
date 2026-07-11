package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeCitationRepository extends JpaRepository<KnowledgeCitationEntity, UUID> {
    List<KnowledgeCitationEntity> findByDocumentId(UUID documentId);
    List<KnowledgeCitationEntity> findByRetrievalRequestId(UUID retrievalRequestId);
    List<KnowledgeCitationEntity> findByDocumentIdOrderByRelevanceScoreDesc(UUID documentId);
}
