package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface KnowledgeCollectionRepository {
    Optional<KnowledgeCollection> findById(KnowledgeCollectionId id);
    List<KnowledgeCollection> findByWorkspaceId(String workspaceId);
    List<KnowledgeCollection> findByOwner(String owner);
    List<KnowledgeCollection> findAll();
    KnowledgeCollection save(KnowledgeCollection collection);
    void delete(KnowledgeCollectionId id);
    boolean exists(KnowledgeCollectionId id);
}
