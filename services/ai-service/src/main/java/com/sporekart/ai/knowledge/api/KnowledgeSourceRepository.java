package com.sporekart.ai.knowledge.api;

import com.sporekart.ai.knowledge.domain.*;
import java.util.*;

public interface KnowledgeSourceRepository {
    Optional<KnowledgeSource> findById(KnowledgeSourceId id);
    List<KnowledgeSource> findByWorkspaceId(String workspaceId);
    List<KnowledgeSource> findByOwner(String owner);
    List<KnowledgeSource> findByStatus(DocumentStatus status);
    List<KnowledgeSource> findAll();
    KnowledgeSource save(KnowledgeSource source);
    void delete(KnowledgeSourceId id);
    boolean exists(KnowledgeSourceId id);
}
