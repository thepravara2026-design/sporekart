package com.sporekart.ai.knowledgeregistry.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeSourceHealthRepository extends JpaRepository<KnowledgeSourceHealthEntity, Long> {

    List<KnowledgeSourceHealthEntity> findBySourceIdOrderByCheckedAtDesc(String sourceId);
}
