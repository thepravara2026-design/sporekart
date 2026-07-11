package com.sporekart.ai.application.service;

import com.sporekart.ai.common.exception.SynchronizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InventorySyncService {
    private static final Logger log = LoggerFactory.getLogger(InventorySyncService.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Map<String, Object> syncQueue = new ConcurrentHashMap<>();
    private final Map<String, Integer> retryCount = new ConcurrentHashMap<>();
    private static final int MAX_RETRIES = 3;

    public InventorySyncService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void syncInventory(String sourceSystem, String targetSystem, Map<String, Object> syncData)
            throws SynchronizationException {
        validateSyncData(syncData);
        String syncKey = UUID.randomUUID().toString();
        try {
            syncQueue.put(syncKey, syncData);
            processSyncWithRetry(syncKey, sourceSystem, targetSystem, syncData);
        } catch (Exception e) {
            handleSyncError(syncKey, sourceSystem, targetSystem, e);
        }
    }

    private void validateSyncData(Map<String, Object> syncData) throws SynchronizationException {
        if (syncData == null || syncData.isEmpty()) {
            throw new SynchronizationException("Invalid sync payload");
        }
        Object itemId = syncData.get("itemId");
        if (!(itemId instanceof String) || ((String) itemId).isBlank()) {
            throw new SynchronizationException("Invalid sync payload: missing itemId");
        }
        Object quantity = syncData.get("quantity");
        if (!(quantity instanceof Number) || ((Number) quantity).intValue() <= 0) {
            throw new SynchronizationException("Invalid sync payload: quantity must be greater than zero");
        }
    }

    private void processSyncWithRetry(String syncKey, String source, String target, Map<String, Object> data)
            throws SynchronizationException {
        int attempt = retryCount.getOrDefault(syncKey, 0);

        try {
            performSync(source, target, data);
            syncQueue.remove(syncKey);
            retryCount.remove(syncKey);
            publishEvent("SyncCompleted", syncKey);
        } catch (Exception e) {
            if (attempt < MAX_RETRIES) {
                retryCount.put(syncKey, attempt + 1);
                long backoffTime = (long) Math.pow(2, attempt) * 1000;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            processSyncWithRetry(syncKey, source, target, data);
                        } catch (Exception ex) {
                            handleSyncError(syncKey, source, target, ex);
                        }
                    }
                }, backoffTime);
            } else {
                handleSyncError(syncKey, source, target, e);
            }
        }
    }

    private void performSync(String source, String target, Map<String, Object> data)
            throws SynchronizationException {
        // Simulate sync operation
        if (data.isEmpty()) {
            throw new SynchronizationException("No data to sync");
        }
        publishEvent("SyncProcessing", source + "->" + target);
    }

    private void handleSyncError(String syncKey, String source, String target, Exception e)
            throws SynchronizationException {
        syncQueue.remove(syncKey);
        retryCount.remove(syncKey);
        String errorMsg = "Sync failed from " + source + " to " + target + ": " + e.getMessage();
        log.error(errorMsg, e);
        publishEvent("SyncFailed", errorMsg);
        throw new SynchronizationException(errorMsg, e);
    }

    public Map<String, Object> getSyncStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("pendingItems", syncQueue.size());
        status.put("totalRetries", retryCount.values().stream().mapToInt(Integer::intValue).sum());
        return status;
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("inventory-sync-events", eventType, eventData);
        } catch (Exception e) {
            log.warn("Failed to publish inventory sync event {}: {}", eventType, e.getMessage());
        }
    }
}
