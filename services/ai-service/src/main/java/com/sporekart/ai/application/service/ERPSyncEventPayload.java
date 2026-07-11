package com.sporekart.ai.application.service;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ERPSyncEventPayload(
        UUID syncId,
        String syncType,
        String erpProvider,
        String status,
        Integer recordsSynced,
        Integer recordsFailed,
        OffsetDateTime syncStartTime,
        OffsetDateTime syncEndTime,
        String errorMessage,
        Integer retryCount,
        Integer maxRetries) {
}
