package com.sporekart.ai.application.service;

import com.sporekart.ai.application.service.ERPSyncEventPayload;
import com.sporekart.ai.common.exception.ERPIntegrationException;
import com.sporekart.ai.domain.model.ERPSyncLog;
import com.sporekart.ai.infrastructure.integration.erp.ERPAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sporekart.ai.infrastructure.integration.erp.ERPAdapterFactory;
import com.sporekart.ai.infrastructure.persistence.entity.ERPSyncLogEntity;
import com.sporekart.ai.infrastructure.persistence.repository.ERPSyncLogRepository;
import com.sporekart.ai.interfaces.rest.dto.ERPSyncHistoryEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class ERPIntegrationService {
    private static final Logger log = LoggerFactory.getLogger(ERPIntegrationService.class);
    private final AiEventPublisher aiEventPublisher;
    private final ERPAdapterFactory adapterFactory;
    private final ERPSyncLogRepository erpSyncLogRepository;
    private final AiCacheService aiCacheService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, String>> erpConfigs = new HashMap<>();
    private final Map<UUID, ERPSyncLog> syncLogs = new HashMap<>();

    public ERPIntegrationService(AiEventPublisher aiEventPublisher,
            ERPAdapterFactory adapterFactory,
            ERPSyncLogRepository erpSyncLogRepository,
            AiCacheService aiCacheService,
            AuditService auditService) {
        this.aiEventPublisher = aiEventPublisher;
        this.adapterFactory = adapterFactory;
        this.erpSyncLogRepository = erpSyncLogRepository;
        this.aiCacheService = aiCacheService;
        this.auditService = auditService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        initializeERPProviders();
    }

    private void initializeERPProviders() {
        erpConfigs.put("TALLY_PRIME", new HashMap<>());
        erpConfigs.put("ZOHO_BOOKS", new HashMap<>());
        erpConfigs.put("ERPNEXT", new HashMap<>());
        erpConfigs.put("SAP", new HashMap<>());
        erpConfigs.put("ORACLE", new HashMap<>());
    }

    public void configureERPProvider(String provider, String endpoint, String apiKey)
            throws ERPIntegrationException {
        Map<String, String> config = getProviderConfig(provider);
        config.put("endpoint", endpoint);
        config.put("apiKey", apiKey);
        config.put("enabled", "true");
    }

    public ERPSyncLog initiateSync(String syncType, String provider) throws ERPIntegrationException {
        Map<String, String> config = getProviderConfig(provider);
        if (!config.containsKey("enabled")) {
            throw new ERPIntegrationException("ERP provider not configured: " + provider);
        }

        ERPSyncLog log = new ERPSyncLog(UUID.randomUUID(), syncType, provider);
        log.startSync();
        ERPSyncLogEntity entity = toEntity(log);
        erpSyncLogRepository.save(entity);
        syncLogs.put(log.getId(), log);
        auditService.logAction("erp_sync", log.getId().toString(), "STARTED", "ERP sync started for " + provider);

        try {
            ERPAdapter adapter = adapterFactory.getAdapter(provider);
            adapter.connect(config);
            publishEvent("ERPSynchronizationStarted", createEventPayload(log));
            switch (syncType) {
                case "INVENTORY" -> adapter.syncInventory(Map.of("syncType", syncType));
                case "ACCOUNTS" -> adapter.syncAccounts(Map.of("syncType", syncType));
                case "SUPPLIERS" -> adapter.syncSuppliers(Map.of("syncType", syncType));
                case "PURCHASES" -> adapter.syncPurchases(Map.of("syncType", syncType));
                case "SALES" -> adapter.syncSales(Map.of("syncType", syncType));
                default -> throw new ERPIntegrationException("Unsupported ERP sync type: " + syncType);
            }
        } catch (ERPIntegrationException e) {
            log.failSync(e.getMessage());
            applyLogToEntity(log, entity);
            erpSyncLogRepository.save(entity);
            publishEvent("ERPSynchronizationFailed", createEventPayload(log));
            throw e;
        } catch (Exception e) {
            log.failSync(e.getMessage());
            applyLogToEntity(log, entity);
            erpSyncLogRepository.save(entity);
            publishEvent("ERPSynchronizationFailed", createEventPayload(log));
            throw new ERPIntegrationException("Failed to initiate ERP sync: " + e.getMessage(), e);
        }

        return log;
    }

    public void completeSyncLog(UUID syncLogId, Integer synced, Integer failed)
            throws ERPIntegrationException {
        ERPSyncLogEntity entity = erpSyncLogRepository.findById(syncLogId)
                .orElseThrow(() -> new ERPIntegrationException("Sync log not found"));
        ERPSyncLog log = syncLogs.computeIfAbsent(syncLogId, id -> toDomain(entity));
        log.completeSync(synced, failed);
        applyLogToEntity(log, entity);
        erpSyncLogRepository.save(entity);
        auditService.logAction("erp_sync", log.getId().toString(), "COMPLETED",
                "ERP sync completed for " + log.getErpProvider());
        publishEvent("ERPSynchronizationCompleted", createEventPayload(log));
    }

    public void failSyncLog(UUID syncLogId, String error) throws ERPIntegrationException {
        ERPSyncLogEntity entity = erpSyncLogRepository.findById(syncLogId)
                .orElseThrow(() -> new ERPIntegrationException("Sync log not found"));
        ERPSyncLog log = syncLogs.computeIfAbsent(syncLogId, id -> toDomain(entity));
        log.failSync(error);
        applyLogToEntity(log, entity);
        erpSyncLogRepository.save(entity);
        if (log.canRetry()) {
            publishEvent("ERPSynchronizationRetry", createEventPayload(log));
        }
    }

    public Map<String, Object> getERPStatus(String provider) throws ERPIntegrationException {
        Map<String, String> config = getProviderConfig(provider);

        String cacheKey = "erp-status:" + provider;
        String cachedStatus = aiCacheService.get(cacheKey);
        if (cachedStatus != null) {
            return Map.of("provider", provider, "isConfigured", config.containsKey("enabled"), "cached", true);
        }

        Map<String, Object> status = new HashMap<>();
        status.put("provider", provider);
        status.put("isConfigured", config.containsKey("enabled"));

        List<Map<String, Object>> recentSyncs = new ArrayList<>();
        for (ERPSyncLogEntity entity : erpSyncLogRepository.findAll()) {
            if (entity.getErpProvider().equals(provider)) {
                Map<String, Object> syncInfo = new HashMap<>();
                syncInfo.put("syncType", entity.getSyncType());
                syncInfo.put("status", entity.getSyncStatus());
                syncInfo.put("recordsSynced", entity.getRecordsSynced());
                recentSyncs.add(syncInfo);
            }
        }

        status.put("recentSyncs", recentSyncs);
        aiCacheService.put(cacheKey, "cached");
        return status;
    }

    public List<ERPSyncHistoryEntry> getSyncHistory(String provider) {
        getProviderConfig(provider);
        List<ERPSyncHistoryEntry> history = new ArrayList<>();
        for (ERPSyncLogEntity entity : erpSyncLogRepository.findAll()) {
            if (entity.getErpProvider().equals(provider)) {
                history.add(toHistoryEntry(entity));
            }
        }
        return history;
    }

    private ERPSyncHistoryEntry toHistoryEntry(ERPSyncLogEntity entity) {
        return new ERPSyncHistoryEntry(
                entity.getId(),
                entity.getSyncType(),
                entity.getErpProvider(),
                entity.getSyncStatus(),
                entity.getRecordsSynced(),
                entity.getRecordsFailed(),
                entity.getSyncStartTime(),
                entity.getSyncEndTime(),
                entity.getErrorMessage());
    }

    private Map<String, String> getProviderConfig(String provider) {
        Map<String, String> config = erpConfigs.get(provider);
        if (config == null) {
            throw new ERPIntegrationException("Unknown ERP provider: " + provider);
        }
        return config;
    }

    private ERPSyncLogEntity toEntity(ERPSyncLog log) {
        ERPSyncLogEntity entity = new ERPSyncLogEntity(log.getId(), log.getSyncType(), log.getErpProvider());
        entity.setSyncStatus(log.getSyncStatus());
        entity.setRecordsSynced(log.getRecordsSynced());
        entity.setRecordsFailed(log.getRecordsFailed());
        entity.setSyncStartTime(log.getSyncStartTime());
        entity.setSyncEndTime(log.getSyncEndTime());
        entity.setErrorMessage(log.getErrorMessage());
        entity.setRetryCount(log.getRetryCount());
        entity.setMaxRetries(log.getMaxRetries());
        entity.setCreatedAt(log.getCreatedAt());
        entity.setUpdatedAt(log.getUpdatedAt());
        return entity;
    }

    private void applyLogToEntity(ERPSyncLog log, ERPSyncLogEntity entity) {
        entity.setSyncStatus(log.getSyncStatus());
        entity.setRecordsSynced(log.getRecordsSynced());
        entity.setRecordsFailed(log.getRecordsFailed());
        entity.setSyncStartTime(log.getSyncStartTime());
        entity.setSyncEndTime(log.getSyncEndTime());
        entity.setErrorMessage(log.getErrorMessage());
        entity.setRetryCount(log.getRetryCount());
        entity.setMaxRetries(log.getMaxRetries());
        entity.setUpdatedAt(log.getUpdatedAt());
    }

    private ERPSyncLog toDomain(ERPSyncLogEntity entity) {
        ERPSyncLog log = new ERPSyncLog(entity.getId(), entity.getSyncType(), entity.getErpProvider(),
                entity.getSyncStatus(), entity.getRecordsSynced(), entity.getRecordsFailed(),
                entity.getSyncStartTime(), entity.getSyncEndTime(), entity.getErrorMessage(),
                entity.getRetryCount(), entity.getMaxRetries(), entity.getCreatedAt(), entity.getUpdatedAt());
        return log;
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            aiEventPublisher.publish(eventType, eventData);
        } catch (Exception e) {
            log.warn("Failed to publish ERP event {}: {}", eventType, e.getMessage());
        }
    }

    private String createEventPayload(ERPSyncLog syncLog) {
        try {
            ERPSyncEventPayload payload = new ERPSyncEventPayload(
                    syncLog.getId(),
                    syncLog.getSyncType(),
                    syncLog.getErpProvider(),
                    syncLog.getSyncStatus(),
                    syncLog.getRecordsSynced(),
                    syncLog.getRecordsFailed(),
                    syncLog.getSyncStartTime(),
                    syncLog.getSyncEndTime(),
                    syncLog.getErrorMessage(),
                    syncLog.getRetryCount(),
                    syncLog.getMaxRetries());
            return objectMapper.writeValueAsString(payload);
        } catch (Exception e) {
            log.warn("Failed to serialize ERP event payload: {}", e.getMessage());
            return syncLog.getId().toString();
        }
    }
}
