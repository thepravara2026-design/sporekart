package com.sporekart.ai.knowledgeregistry.api;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;

import java.util.List;

public interface KnowledgeRegistryService {

    KnowledgeSourceEntry registerSource(KnowledgeSourceEntry entry);

    KnowledgeSourceEntry updateSource(String sourceId, KnowledgeSourceEntry entry);

    KnowledgeSourceEntry getSource(String sourceId);

    List<KnowledgeSourceEntry> listSources();

    List<KnowledgeSourceEntry> searchSources(String name);

    List<KnowledgeSourceEntry> getByType(KnowledgeSourceType sourceType);

    List<KnowledgeSourceEntry> getByOwner(String owner);

    List<KnowledgeSourceEntry> getBySyncStatus(SyncStatus syncStatus);
}
