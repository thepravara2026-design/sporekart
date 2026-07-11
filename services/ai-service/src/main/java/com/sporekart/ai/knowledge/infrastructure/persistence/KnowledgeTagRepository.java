package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeTagRepository extends JpaRepository<KnowledgeTagEntity, UUID> {
    List<KnowledgeTagEntity> findByDocumentId(UUID documentId);
    List<KnowledgeTagEntity> findByTag(String tag);
    void deleteByDocumentId(UUID documentId);
}
