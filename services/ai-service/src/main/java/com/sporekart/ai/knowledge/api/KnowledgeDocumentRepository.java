package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface KnowledgeDocumentRepository {
    Optional<KnowledgeDocument> findById(KnowledgeDocumentId id);
    List<KnowledgeDocument> findBySourceId(KnowledgeSourceId sourceId);
    List<KnowledgeDocument> findByCollectionId(KnowledgeCollectionId collectionId);
    List<KnowledgeDocument> findByStatus(DocumentStatus status);
    List<KnowledgeDocument> findByWorkspaceId(String workspaceId);
    List<KnowledgeDocument> findAll();
    KnowledgeDocument save(KnowledgeDocument document);
    void delete(KnowledgeDocumentId id);
    boolean exists(KnowledgeDocumentId id);
}
