package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ERPSyncHistoryEntry {
    @Schema(description = "ERP sync identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID syncId;

    @Schema(description = "ERP sync type", example = "SALES")
    private String syncType;

    @Schema(description = "ERP provider", example = "SAP")
    private String erpProvider;

    @Schema(description = "Sync status", example = "SUCCESS")
    private String status;

    @Schema(description = "Number of records successfully synced", example = "120")
    private Integer recordsSynced;

    @Schema(description = "Number of records that failed during sync", example = "3")
    private Integer recordsFailed;

    @Schema(description = "Sync start timestamp")
    private OffsetDateTime syncStartTime;

    @Schema(description = "Sync end timestamp")
    private OffsetDateTime syncEndTime;

    @Schema(description = "Error message if sync failed", example = "network error")
    private String errorMessage;

    public ERPSyncHistoryEntry() {
    }

    public ERPSyncHistoryEntry(UUID syncId, String syncType, String erpProvider, String status,
            Integer recordsSynced, Integer recordsFailed, OffsetDateTime syncStartTime,
            OffsetDateTime syncEndTime, String errorMessage) {
        this.syncId = syncId;
        this.syncType = syncType;
        this.erpProvider = erpProvider;
        this.status = status;
        this.recordsSynced = recordsSynced;
        this.recordsFailed = recordsFailed;
        this.syncStartTime = syncStartTime;
        this.syncEndTime = syncEndTime;
        this.errorMessage = errorMessage;
    }

    public UUID getSyncId() {
        return syncId;
    }

    public void setSyncId(UUID syncId) {
        this.syncId = syncId;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getErpProvider() {
        return erpProvider;
    }

    public void setErpProvider(String erpProvider) {
        this.erpProvider = erpProvider;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRecordsSynced() {
        return recordsSynced;
    }

    public void setRecordsSynced(Integer recordsSynced) {
        this.recordsSynced = recordsSynced;
    }

    public Integer getRecordsFailed() {
        return recordsFailed;
    }

    public void setRecordsFailed(Integer recordsFailed) {
        this.recordsFailed = recordsFailed;
    }

    public OffsetDateTime getSyncStartTime() {
        return syncStartTime;
    }

    public void setSyncStartTime(OffsetDateTime syncStartTime) {
        this.syncStartTime = syncStartTime;
    }

    public OffsetDateTime getSyncEndTime() {
        return syncEndTime;
    }

    public void setSyncEndTime(OffsetDateTime syncEndTime) {
        this.syncEndTime = syncEndTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
