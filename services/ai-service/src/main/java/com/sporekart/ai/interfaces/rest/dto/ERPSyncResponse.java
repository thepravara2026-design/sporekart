package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class ERPSyncResponse {
    @Schema(description = "Sync identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID syncId;

    @Schema(description = "Sync status", example = "IN_PROGRESS")
    private String status;

    public ERPSyncResponse() {
    }

    public ERPSyncResponse(UUID syncId, String status) {
        this.syncId = syncId;
        this.status = status;
    }

    public UUID getSyncId() {
        return syncId;
    }

    public void setSyncId(UUID syncId) {
        this.syncId = syncId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
