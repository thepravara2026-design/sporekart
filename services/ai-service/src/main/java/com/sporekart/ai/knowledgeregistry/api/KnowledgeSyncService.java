package com.sporekart.ai.knowledgeregistry.api;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;

import java.util.List;

public interface KnowledgeSyncService {

    KnowledgeSourceEntry triggerSync(String sourceId);

    SyncStatus getSyncStatus(String sourceId);

    List<KnowledgeSourceEntry> getSyncHistory(String sourceId);
}
