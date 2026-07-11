package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeAccessLogRepository extends JpaRepository<KnowledgeAccessLogEntity, UUID> {
    List<KnowledgeAccessLogEntity> findByDocumentIdOrderByTimestampDesc(UUID documentId);
    List<KnowledgeAccessLogEntity> findByActionOrderByTimestampDesc(String action);
    List<KnowledgeAccessLogEntity> findByUserIdOrderByTimestampDesc(UUID userId);
    long countByAction(String action);
}
