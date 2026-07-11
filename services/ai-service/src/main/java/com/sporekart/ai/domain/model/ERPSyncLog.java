package com.sporekart.ai.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ERPSyncLog {
    private UUID id;
    private String syncType; // INVENTORY, SALES, PURCHASES, ACCOUNTS, SUPPLIERS
    private String erpProvider; // TALLY_PRIME, ZOHO_BOOKS, ERPNEXT, SAP, ORACLE
    private String syncStatus; // PENDING, IN_PROGRESS, SUCCESS, FAILURE
    private Integer recordsSynced;
    private Integer recordsFailed;
    private OffsetDateTime syncStartTime;
    private OffsetDateTime syncEndTime;
    private String errorMessage;
    private Integer retryCount;
    private Integer maxRetries;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public ERPSyncLog(UUID id, String syncType, String erpProvider) {
        this.id = id;
        this.syncType = syncType;
        this.erpProvider = erpProvider;
        this.syncStatus = "PENDING";
        this.recordsSynced = 0;
        this.recordsFailed = 0;
        this.retryCount = 0;
        this.maxRetries = 5;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getSyncType() {
        return syncType;
    }

    public String getErpProvider() {
        return erpProvider;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public Integer getRecordsSynced() {
        return recordsSynced;
    }

    public Integer getRecordsFailed() {
        return recordsFailed;
    }

    public OffsetDateTime getSyncStartTime() {
        return syncStartTime;
    }

    public OffsetDateTime getSyncEndTime() {
        return syncEndTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ERPSyncLog(UUID id, String syncType, String erpProvider, String syncStatus,
            Integer recordsSynced, Integer recordsFailed, OffsetDateTime syncStartTime,
            OffsetDateTime syncEndTime, String errorMessage, Integer retryCount,
            Integer maxRetries, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.syncType = syncType;
        this.erpProvider = erpProvider;
        this.syncStatus = syncStatus;
        this.recordsSynced = recordsSynced;
        this.recordsFailed = recordsFailed;
        this.syncStartTime = syncStartTime;
        this.syncEndTime = syncEndTime;
        this.errorMessage = errorMessage;
        this.retryCount = retryCount;
        this.maxRetries = maxRetries;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void startSync() {
        this.syncStatus = "IN_PROGRESS";
        this.syncStartTime = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void completeSync(Integer synced, Integer failed) {
        this.syncStatus = "SUCCESS";
        this.recordsSynced = synced;
        this.recordsFailed = failed;
        this.syncEndTime = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void failSync(String error) {
        this.syncStatus = "FAILURE";
        this.errorMessage = error;
        this.syncEndTime = OffsetDateTime.now();
        this.retryCount++;
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean canRetry() {
        return retryCount < maxRetries;
    }
}
