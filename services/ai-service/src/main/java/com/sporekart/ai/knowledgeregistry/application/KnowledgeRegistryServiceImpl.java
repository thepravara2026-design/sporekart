package com.sporekart.ai.knowledgeregistry.application;

import com.sporekart.ai.knowledgeregistry.api.KnowledgeRegistryService;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.SourceHealthStatus;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KnowledgeRegistryServiceImpl implements KnowledgeRegistryService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRegistryServiceImpl.class);

    private final KnowledgeSourceRepository sourceRepository;

    public KnowledgeRegistryServiceImpl(KnowledgeSourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    @Override
    @Transactional
    public KnowledgeSourceEntry registerSource(KnowledgeSourceEntry entry) {
        KnowledgeSourceEntity entity = new KnowledgeSourceEntity();
        String sourceId = entry.getSourceId();
        if (sourceId == null || sourceId.isBlank()) {
            sourceId = UUID.randomUUID().toString();
        }
        entity.setSourceId(sourceId);
        applyToEntity(entry, entity);
        Instant now = Instant.now();
        entity.setVersion(1);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        if (entity.getHealthStatus() == null) {
            entity.setHealthStatus(SourceHealthStatus.UNKNOWN);
        }
        if (entity.getSyncStatus() == null) {
            entity.setSyncStatus(SyncStatus.PENDING);
        }
        KnowledgeSourceEntity saved = sourceRepository.save(entity);
        log.info("Registered knowledge source: {}", saved.getSourceId());
        return toDomain(saved);
    }

    @Override
    @Transactional
    public KnowledgeSourceEntry updateSource(String sourceId, KnowledgeSourceEntry entry) {
        KnowledgeSourceEntity entity = sourceRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
        applyToEntity(entry, entity);
        entity.setVersion(entity.getVersion() + 1);
        entity.setUpdatedAt(Instant.now());
        KnowledgeSourceEntity saved = sourceRepository.save(entity);
        log.info("Updated knowledge source: {}", saved.getSourceId());
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public KnowledgeSourceEntry getSource(String sourceId) {
        return sourceRepository.findById(sourceId)
                .map(this::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("Knowledge source not found: " + sourceId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> listSources() {
        return sourceRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> searchSources(String name) {
        return sourceRepository.findBySourceNameContainingIgnoreCase(name == null ? "" : name).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> getByType(KnowledgeSourceType sourceType) {
        return sourceRepository.findBySourceType(sourceType).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> getByOwner(String owner) {
        return sourceRepository.findByOwner(owner).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<KnowledgeSourceEntry> getBySyncStatus(SyncStatus syncStatus) {
        return sourceRepository.findBySyncStatus(syncStatus).stream()
                .map(this::toDomain).collect(Collectors.toList());
    }

    private void applyToEntity(KnowledgeSourceEntry entry, KnowledgeSourceEntity entity) {
        entity.setSourceName(entry.getSourceName());
        entity.setSourceType(entry.getSourceType());
        entity.setDescription(entry.getDescription());
        entity.setOwner(entry.getOwner());
        entity.setMetadata(entry.getMetadata() == null ? new HashMap<>() : new HashMap<>(entry.getMetadata()));
        entity.setRefreshPolicy(entry.getRefreshPolicy());
        entity.setRefreshCron(entry.getRefreshCron());
        if (entry.getHealthStatus() != null) {
            entity.setHealthStatus(entry.getHealthStatus());
        }
        if (entry.getSyncStatus() != null) {
            entity.setSyncStatus(entry.getSyncStatus());
        }
        if (entry.getNextSyncAt() != null) {
            entity.setNextSyncAt(entry.getNextSyncAt());
        }
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
