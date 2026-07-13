package com.sporekart.ai.knowledgeregistry.domain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class KnowledgeSourceEntry {

    private String sourceId;
    private String sourceName;
    private KnowledgeSourceType sourceType;
    private String description;
    private String owner;
    private int version;
    private Map<String, String> metadata = new HashMap<>();
    private RefreshPolicy refreshPolicy;
    private String refreshCron;
    private SourceHealthStatus healthStatus;
    private SyncStatus syncStatus;
    private Instant lastSyncAt;
    private Instant nextSyncAt;
    private Instant createdAt;
    private Instant updatedAt;

    public KnowledgeSourceEntry() {
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public KnowledgeSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(KnowledgeSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public RefreshPolicy getRefreshPolicy() {
        return refreshPolicy;
    }

    public void setRefreshPolicy(RefreshPolicy refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
    }

    public String getRefreshCron() {
        return refreshCron;
    }

    public void setRefreshCron(String refreshCron) {
        this.refreshCron = refreshCron;
    }

    public SourceHealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(SourceHealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Instant getLastSyncAt() {
        return lastSyncAt;
    }

    public void setLastSyncAt(Instant lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }

    public Instant getNextSyncAt() {
        return nextSyncAt;
    }

    public void setNextSyncAt(Instant nextSyncAt) {
        this.nextSyncAt = nextSyncAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
