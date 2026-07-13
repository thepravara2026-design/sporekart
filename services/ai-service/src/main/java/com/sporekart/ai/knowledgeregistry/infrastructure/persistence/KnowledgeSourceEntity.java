package com.sporekart.ai.knowledgeregistry.infrastructure.persistence;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.RefreshPolicy;
import com.sporekart.ai.knowledgeregistry.domain.SourceHealthStatus;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "kr_knowledge_source")
public class KnowledgeSourceEntity {

    @Id
    @Column(name = "source_id", nullable = false, length = 255)
    private String sourceId;

    @Column(name = "source_name", nullable = false, length = 255)
    private String sourceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 64)
    private KnowledgeSourceType sourceType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "owner", length = 255)
    private String owner;

    @Column(name = "version", nullable = false)
    private int version;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "kr_knowledge_source_metadata",
            joinColumns = @JoinColumn(name = "source_id"))
    @MapKeyColumn(name = "metadata_key", length = 255)
    @Column(name = "metadata_value", length = 2048)
    private Map<String, String> metadata = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "refresh_policy", length = 32)
    private RefreshPolicy refreshPolicy;

    @Column(name = "refresh_cron", length = 128)
    private String refreshCron;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status", length = 32)
    private SourceHealthStatus healthStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status", length = 32)
    private SyncStatus syncStatus;

    @Column(name = "last_sync_at")
    private Instant lastSyncAt;

    @Column(name = "next_sync_at")
    private Instant nextSyncAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public KnowledgeSourceEntity() {
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
