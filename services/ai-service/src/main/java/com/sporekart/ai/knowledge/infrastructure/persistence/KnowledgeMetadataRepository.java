package com.sporekart.ai.knowledge.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KnowledgeMetadataRepository extends JpaRepository<KnowledgeMetadataEntity, UUID> {
    List<KnowledgeMetadataEntity> findByDocumentId(UUID documentId);
    void deleteByDocumentId(UUID documentId);
}
