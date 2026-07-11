package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.KnowledgeDocument;

import java.util.List;
import java.util.Optional;

public interface KnowledgeDocumentPort {
    KnowledgeDocument save(KnowledgeDocument document);
    Optional<KnowledgeDocument> findById(String id);
    List<KnowledgeDocument> findAll();
    List<KnowledgeDocument> findByCategory(String category);
    void deleteById(String id);
}
