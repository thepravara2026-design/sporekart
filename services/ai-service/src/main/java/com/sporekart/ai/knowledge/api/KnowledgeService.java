package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.KnowledgeDocument;

import java.util.List;
import java.util.Optional;

public interface KnowledgeService {
    KnowledgeDocument createDocument(KnowledgeDocument document);
    KnowledgeDocument updateDocument(KnowledgeDocument document);
    Optional<KnowledgeDocument> findById(String id);
    List<KnowledgeDocument> findByCategory(String category);
    List<KnowledgeDocument> search(String query);
    void deleteDocument(String id);
}
