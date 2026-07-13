package com.sporekart.ai.knowledgeregistry.infrastructure.persistence;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeSourceRepository extends JpaRepository<KnowledgeSourceEntity, String> {

    List<KnowledgeSourceEntity> findBySourceType(KnowledgeSourceType sourceType);

    List<KnowledgeSourceEntity> findByOwner(String owner);

    List<KnowledgeSourceEntity> findBySyncStatus(SyncStatus syncStatus);

    List<KnowledgeSourceEntity> findBySourceNameContainingIgnoreCase(String sourceName);
}
