package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class OfflineSyncLog {
    private UUID id;
    private UUID deviceId;
    private String status;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private String errorMessage;
    private int recordsSync;
    private int bytesSync;

    public OfflineSyncLog(UUID id, UUID deviceId, String status,
            OffsetDateTime startedAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.status = status;
        this.startedAt = startedAt;
        this.recordsSync = 0;
        this.bytesSync = 0;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getStartedAt() {
        return startedAt;
    }

    public OffsetDateTime getCompletedAt() {
        return completedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getRecordsSync() {
        return recordsSync;
    }

    public int getBytesSync() {
        return bytesSync;
    }

    public void complete(int recordsSync, int bytesSync) {
        this.status = "SUCCESS";
        this.completedAt = OffsetDateTime.now();
        this.recordsSync = recordsSync;
        this.bytesSync = bytesSync;
    }

    public void fail(String errorMessage) {
        this.status = "FAILED";
        this.completedAt = OffsetDateTime.now();
        this.errorMessage = errorMessage;
    }
}
