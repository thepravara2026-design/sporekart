package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "erp_sync_logs")
public class ERPSyncLogEntity {
    @Id
    private UUID id;

    @Column(name = "sync_type")
    private String syncType;

    @Column(name = "erp_provider")
    private String erpProvider;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "records_synced")
    private Integer recordsSynced;

    @Column(name = "records_failed")
    private Integer recordsFailed;

    @Column(name = "sync_start_time")
    private OffsetDateTime syncStartTime;

    @Column(name = "sync_end_time")
    private OffsetDateTime syncEndTime;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "max_retries")
    private Integer maxRetries;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public ERPSyncLogEntity() {
    }

    public ERPSyncLogEntity(UUID id, String syncType, String erpProvider) {
        this.id = id;
        this.syncType = syncType;
        this.erpProvider = erpProvider;
        this.syncStatus = "PENDING";
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

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public void setRecordsSynced(Integer recordsSynced) {
        this.recordsSynced = recordsSynced;
    }

    public void setRecordsFailed(Integer recordsFailed) {
        this.recordsFailed = recordsFailed;
    }

    public void setSyncStartTime(OffsetDateTime syncStartTime) {
        this.syncStartTime = syncStartTime;
    }

    public void setSyncEndTime(OffsetDateTime syncEndTime) {
        this.syncEndTime = syncEndTime;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
