package com.sporekart.ai.knowledgeregistry.interfaces.rest.dto;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;

import java.time.Instant;

public class KnowledgeSyncResponseDto {

    private String sourceId;
    private SyncStatus syncStatus;
    private Instant lastSyncAt;
    private Instant nextSyncAt;

    public KnowledgeSyncResponseDto() {
    }

    public static KnowledgeSyncResponseDto from(KnowledgeSourceEntry entry) {
        KnowledgeSyncResponseDto dto = new KnowledgeSyncResponseDto();
        dto.setSourceId(entry.getSourceId());
        dto.setSyncStatus(entry.getSyncStatus());
        dto.setLastSyncAt(entry.getLastSyncAt());
        dto.setNextSyncAt(entry.getNextSyncAt());
        return dto;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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
}
