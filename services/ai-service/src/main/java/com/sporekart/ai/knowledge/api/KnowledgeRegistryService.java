package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface KnowledgeRegistryService {
    KnowledgeSource registerSource(String name, String description, String workspaceId, String owner, DocumentType sourceType, String sourceUrl, String language);
    KnowledgeSource getSource(KnowledgeSourceId id);
    List<KnowledgeSource> listSources(String workspaceId, DocumentStatus status);
    KnowledgeSource updateSource(KnowledgeSourceId id, String name, String description, String sourceUrl, String language);
    void archiveSource(KnowledgeSourceId id);
    void publishSource(KnowledgeSourceId id);
    void deleteSource(KnowledgeSourceId id);
    KnowledgeCollection createCollection(String name, String description, String workspaceId, String owner);
    KnowledgeCollection getCollection(KnowledgeCollectionId id);
    List<KnowledgeCollection> listCollections(String workspaceId);
    void deleteCollection(KnowledgeCollectionId id);
}
