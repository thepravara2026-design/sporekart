package com.sporekart.ai.knowledgeregistry.application;

import com.sporekart.ai.knowledgeregistry.api.KnowledgeSyncService;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceEntity;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
public class KnowledgeSyncServiceImpl implements KnowledgeSyncService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeSyncServiceImpl.class);

    private final KnowledgeSourceRepository sourceRepository;

    public KnowledgeSyncServiceImpl(KnowledgeSourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    @Transactional
    public KnowledgeSourceEntry triggerSync(String sourceId) {
        KnowledgeSourceEntity entity = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        Instant now = Instant.now();
        entity.setSyncStatus(SyncStatus.IN_PROGRESS);
        entity.setLastSyncAt(now);
        entity.setUpdatedAt(now);
        KnowledgeSourceEntity saved = sourceRepository.save(entity);
        log.info("Triggered sync for knowledge source: {}", saved.getSourceId());
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SyncStatus getSyncStatus(String sourceId) {
        KnowledgeSourceEntity entity = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        return entity.getSyncStatus();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> getSyncHistory(String sourceId) {
        KnowledgeSourceEntity entity = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        return List.of(toDomain(entity));
    }

    private KnowledgeSourceEntry toDomain(KnowledgeSourceEntity entity) {
        KnowledgeSourceEntry entry = new KnowledgeSourceEntry();
        entry.setSourceId(entity.getSourceId());
        entry.setSourceName(entity.getSourceName());
        entry.setSourceType(entity.getSourceType());
        entry.setDescription(entity.getDescription());
        entry.setOwner(entity.getOwner());
        entry.setVersion(entity.getVersion());
        entry.setMetadata(entity.getMetadata() == null ? new HashMap<>() : new HashMap<>(entity.getMetadata()));
        entry.setRefreshPolicy(entity.getRefreshPolicy());
        entry.setRefreshCron(entity.getRefreshCron());
        entry.setHealthStatus(entity.getHealthStatus());
        entry.setSyncStatus(entity.getSyncStatus());
        entry.setLastSyncAt(entity.getLastSyncAt());
        entry.setNextSyncAt(entity.getNextSyncAt());
        entry.setCreatedAt(entity.getCreatedAt());
        entry.setUpdatedAt(entity.getUpdatedAt());
        return entry;
    }
}
