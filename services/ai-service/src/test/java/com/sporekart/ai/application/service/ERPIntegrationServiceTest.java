package com.sporekart.ai.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporekart.ai.common.exception.ERPIntegrationException;
import com.sporekart.ai.domain.model.ERPSyncLog;
import com.sporekart.ai.infrastructure.integration.erp.ERPAdapter;
import com.sporekart.ai.infrastructure.integration.erp.ERPAdapterFactory;
import com.sporekart.ai.infrastructure.persistence.entity.ERPSyncLogEntity;
import com.sporekart.ai.infrastructure.persistence.repository.ERPSyncLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ERPIntegrationServiceTest {
    @Mock
    private com.sporekart.ai.application.service.AiEventPublisher aiEventPublisher;

    @Mock
    private ERPAdapterFactory adapterFactory;

    @Mock
    private ERPAdapter adapter;

    @Mock
    private ERPSyncLogRepository erpSyncLogRepository;

    @Mock
    private AiCacheService aiCacheService;

    @Mock
    private AuditService auditService;

    private ERPIntegrationService erpIntegrationService;
    private final List<ERPSyncLogEntity> savedSyncLogs = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        lenient().when(adapterFactory.getAdapter(anyString())).thenReturn(adapter);
        lenient().when(erpSyncLogRepository.save(any(ERPSyncLogEntity.class))).thenAnswer(invocation -> {
            ERPSyncLogEntity entity = invocation.getArgument(0);
            savedSyncLogs.removeIf(saved -> saved.getId().equals(entity.getId()));
            savedSyncLogs.add(entity);
            return entity;
        });
        lenient().when(erpSyncLogRepository.findAll()).thenAnswer(invocation -> new ArrayList<>(savedSyncLogs));
        lenient().when(erpSyncLogRepository.findById(any(UUID.class))).thenAnswer(invocation -> {
            UUID id = invocation.getArgument(0);
            return savedSyncLogs.stream().filter(saved -> saved.getId().equals(id)).findFirst();
        });
        erpIntegrationService = new ERPIntegrationService(aiEventPublisher, adapterFactory, erpSyncLogRepository,
                aiCacheService, auditService);
    }

    @Test
    void shouldConfigureAndValidateProvider() {
        erpIntegrationService.configureERPProvider("TALLY_PRIME", "https://tally.example.com", "placeholder-secret-key");

        Map<String, Object> status = erpIntegrationService.getERPStatus("TALLY_PRIME");

        assertThat(status).containsEntry("provider", "TALLY_PRIME");
        assertThat(status).containsEntry("isConfigured", true);
        assertThat(status).containsKey("recentSyncs");
    }

    @Test
    void shouldStartSyncAndRecordHistory() {
        erpIntegrationService.configureERPProvider("ERPNEXT", "https://erp.example.com", "erp-key");

        ERPSyncLog syncLog = erpIntegrationService.initiateSync("INVENTORY", "ERPNEXT");

        assertThat(syncLog.getSyncStatus()).isEqualTo("IN_PROGRESS");
        assertThat(syncLog.getErpProvider()).isEqualTo("ERPNEXT");

        List<com.sporekart.ai.interfaces.rest.dto.ERPSyncHistoryEntry> history = erpIntegrationService
                .getSyncHistory("ERPNEXT");

        assertThat(history).hasSize(1);
        assertThat(history.get(0).getSyncId()).isEqualTo(syncLog.getId());
    }

    @Test
    void shouldPublishEventWhenSyncStarts() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");

        ERPSyncLog syncLog = erpIntegrationService.initiateSync("SALES", "SAP");

        assertThat(syncLog.getSyncStatus()).isEqualTo("IN_PROGRESS");

        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(aiEventPublisher).publish(eq("ERPSynchronizationStarted"), valueCaptor.capture());

        JsonNode payload = new ObjectMapper().readTree(valueCaptor.getValue().toString());
        assertThat(payload.get("syncId").asText()).isEqualTo(syncLog.getId().toString());
        assertThat(payload.get("status").asText()).isEqualTo("IN_PROGRESS");
        assertThat(payload.get("syncType").asText()).isEqualTo("SALES");
        assertThat(payload.get("erpProvider").asText()).isEqualTo("SAP");
    }

    @Test
    void shouldPublishSyncStartedBeforeAdapterSync() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        doNothing().when(adapter)
                .connect(Map.of("endpoint", "https://sap.example.com", "apiKey", "sap-key", "enabled", "true"));

        erpIntegrationService.initiateSync("SALES", "SAP");

        verify(aiEventPublisher).publish(eq("ERPSynchronizationStarted"), any());
        verify(adapter).syncSales(Map.of("syncType", "SALES"));
    }

    @Test
    void shouldPublishCorrectEventPayloadForSyncStarted() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");

        ERPSyncLog syncLog = erpIntegrationService.initiateSync("SALES", "SAP");

        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(aiEventPublisher).publish(eq("ERPSynchronizationStarted"), valueCaptor.capture());

        JsonNode payload = new ObjectMapper().readTree(valueCaptor.getValue().toString());
        assertThat(payload.get("syncId").asText()).isEqualTo(syncLog.getId().toString());
        assertThat(payload.get("status").asText()).isEqualTo("IN_PROGRESS");
        assertThat(payload.get("syncType").asText()).isEqualTo("SALES");
        assertThat(payload.get("erpProvider").asText()).isEqualTo("SAP");
        assertThat(payload.get("recordsSynced").asInt()).isEqualTo(0);
        assertThat(payload.get("recordsFailed").asInt()).isEqualTo(0);
    }

    @Test
    void shouldPublishCorrectEventPayloadForSyncFailed() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        doNothing().when(adapter)
                .connect(Map.of("endpoint", "https://sap.example.com", "apiKey", "sap-key", "enabled", "true"));
        doThrow(new RuntimeException("sync timeout")).when(adapter).syncSales(Map.of("syncType", "SALES"));

        assertThatThrownBy(() -> erpIntegrationService.initiateSync("SALES", "SAP"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Failed to initiate ERP sync");

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);

        verify(aiEventPublisher, times(2)).publish(keyCaptor.capture(), valueCaptor.capture());

        assertThat(keyCaptor.getAllValues()).containsExactly("ERPSynchronizationStarted", "ERPSynchronizationFailed");

        JsonNode failedPayload = new ObjectMapper().readTree(valueCaptor.getAllValues().get(1).toString());
        assertThat(failedPayload.get("syncId").asText()).isNotBlank();
        assertThat(failedPayload.get("status").asText()).isEqualTo("FAILURE");
        assertThat(failedPayload.get("errorMessage").asText()).isEqualTo("sync timeout");
        assertThat(failedPayload.get("recordsSynced").asInt()).isEqualTo(0);
        assertThat(failedPayload.get("retryCount").asInt()).isEqualTo(1);
        assertThat(failedPayload.get("maxRetries").asInt()).isEqualTo(5);
    }

    @Test
    void shouldInitiateSalesSyncWithProvider() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");

        erpIntegrationService.initiateSync("SALES", "SAP");

        verify(adapterFactory).getAdapter("SAP");
        verify(adapter).connect(Map.of("endpoint", "https://sap.example.com", "apiKey", "sap-key", "enabled", "true"));
        verify(adapter).syncSales(Map.of("syncType", "SALES"));
    }

    @Test
    void shouldCompleteSyncLog() {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        ERPSyncLog syncLog = erpIntegrationService.initiateSync("SALES", "SAP");

        erpIntegrationService.completeSyncLog(syncLog.getId(), 120, 3);

        assertThat(syncLog.getSyncStatus()).isEqualTo("SUCCESS");
        assertThat(syncLog.getRecordsSynced()).isEqualTo(120);
        assertThat(syncLog.getRecordsFailed()).isEqualTo(3);
    }

    @Test
    void shouldPublishCorrectEventPayloadForSyncCompleted() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        ERPSyncLog syncLog = erpIntegrationService.initiateSync("SALES", "SAP");

        erpIntegrationService.completeSyncLog(syncLog.getId(), 120, 3);

        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(aiEventPublisher).publish(eq("ERPSynchronizationCompleted"), valueCaptor.capture());

        JsonNode payload = new ObjectMapper().readTree(valueCaptor.getValue().toString());
        assertThat(payload.get("syncId").asText()).isEqualTo(syncLog.getId().toString());
        assertThat(payload.get("status").asText()).isEqualTo("SUCCESS");
        assertThat(payload.get("recordsSynced").asInt()).isEqualTo(120);
        assertThat(payload.get("recordsFailed").asInt()).isEqualTo(3);
        assertThat(payload.get("syncEndTime")).isNotNull();
    }

    @Test
    void shouldFailSyncLogAndAllowRetry() {
        erpIntegrationService.configureERPProvider("ZOHO_BOOKS", "https://zoho.example.com", "zoho-key");
        ERPSyncLog syncLog = erpIntegrationService.initiateSync("PURCHASES", "ZOHO_BOOKS");

        erpIntegrationService.failSyncLog(syncLog.getId(), "network error");

        assertThat(syncLog.getSyncStatus()).isEqualTo("FAILURE");
        assertThat(syncLog.canRetry()).isTrue();
    }

    @Test
    void shouldPublishCorrectEventPayloadForSyncRetry() throws Exception {
        erpIntegrationService.configureERPProvider("ZOHO_BOOKS", "https://zoho.example.com", "zoho-key");
        ERPSyncLog syncLog = erpIntegrationService.initiateSync("PURCHASES", "ZOHO_BOOKS");

        erpIntegrationService.failSyncLog(syncLog.getId(), "network error");

        assertThat(syncLog.getSyncStatus()).isEqualTo("FAILURE");
        assertThat(syncLog.canRetry()).isTrue();

        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(aiEventPublisher).publish(eq("ERPSynchronizationRetry"), valueCaptor.capture());

        JsonNode payload = new ObjectMapper().readTree(valueCaptor.getValue().toString());
        assertThat(payload.get("syncId").asText()).isEqualTo(syncLog.getId().toString());
        assertThat(payload.get("status").asText()).isEqualTo("FAILURE");
        assertThat(payload.get("errorMessage").asText()).isEqualTo("network error");
        assertThat(payload.get("retryCount").asInt()).isEqualTo(1);
        assertThat(payload.get("maxRetries").asInt()).isEqualTo(5);
    }

    @Test
    void shouldFailInitiateSyncWhenAdapterConnectThrows() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        doThrow(new RuntimeException("connection refused")).when(adapter)
                .connect(Map.of("endpoint", "https://sap.example.com", "apiKey", "sap-key", "enabled", "true"));

        assertThatThrownBy(() -> erpIntegrationService.initiateSync("INVENTORY", "SAP"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Failed to initiate ERP sync")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(aiEventPublisher).publish(eq("ERPSynchronizationFailed"), any());
    }

    @Test
    void shouldFailInitiateSyncWhenAdapterSyncAccountsThrows() throws Exception {
        erpIntegrationService.configureERPProvider("ORACLE", "https://oracle.example.com", "oracle-key");
        doNothing().when(adapter)
                .connect(Map.of("endpoint", "https://oracle.example.com", "apiKey", "oracle-key", "enabled", "true"));
        doThrow(new RuntimeException("sync timeout")).when(adapter).syncAccounts(Map.of("syncType", "ACCOUNTS"));

        assertThatThrownBy(() -> erpIntegrationService.initiateSync("ACCOUNTS", "ORACLE"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Failed to initiate ERP sync")
                .hasCauseInstanceOf(RuntimeException.class);

        verify(aiEventPublisher).publish(eq("ERPSynchronizationFailed"), any());
    }

    @Test
    void shouldFailInitiateSyncWhenAdapterFactoryThrows() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");
        when(adapterFactory.getAdapter("SAP")).thenThrow(new Exception("factory error"));

        assertThatThrownBy(() -> erpIntegrationService.initiateSync("INVENTORY", "SAP"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Failed to initiate ERP sync")
                .hasCauseInstanceOf(Exception.class);

        verify(aiEventPublisher).publish(eq("ERPSynchronizationFailed"), any());
    }

    @Test
    void shouldThrowWhenInitiatingUnconfiguredProvider() {
        assertThatThrownBy(() -> erpIntegrationService.initiateSync("INVENTORY", "SAP"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("ERP provider not configured");
    }

    @Test
    void shouldThrowWhenSyncTypeUnsupported() throws Exception {
        erpIntegrationService.configureERPProvider("SAP", "https://sap.example.com", "sap-key");

        assertThatThrownBy(() -> erpIntegrationService.initiateSync("UNKNOWN_TYPE", "SAP"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Unsupported ERP sync type");

        verify(aiEventPublisher).publish(eq("ERPSynchronizationFailed"), any());
    }

    @Test
    void shouldThrowWhenCompletingUnknownSyncLog() {
        assertThatThrownBy(() -> erpIntegrationService.completeSyncLog(UUID.randomUUID(), 10, 0))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Sync log not found");
    }

    @Test
    void shouldThrowWhenFailingUnknownSyncLog() {
        assertThatThrownBy(() -> erpIntegrationService.failSyncLog(UUID.randomUUID(), "error"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Sync log not found");
    }

    @Test
    void shouldThrowForUnknownProvider() {
        assertThatThrownBy(() -> erpIntegrationService.configureERPProvider("UNKNOWN", "https://invalid", "key"))
                .isInstanceOf(ERPIntegrationException.class)
                .hasMessageContaining("Unknown ERP provider");
    }

    @Test
    void shouldLogEventPublishFailureWithoutFailing() throws Exception {
        doThrow(new RuntimeException("kafka down")).when(aiEventPublisher).publish(eq("ERPSynchronizationStarted"),
                any());

        erpIntegrationService.configureERPProvider("ORACLE", "https://oracle.example.com", "oracle-key");
        ERPSyncLog syncLog = erpIntegrationService.initiateSync("ACCOUNTS", "ORACLE");

        assertThat(syncLog.getSyncStatus()).isEqualTo("IN_PROGRESS");

        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(aiEventPublisher).publish(eq("ERPSynchronizationStarted"), valueCaptor.capture());

        JsonNode payload = new ObjectMapper().readTree(valueCaptor.getValue().toString());
        assertThat(payload.get("syncId").asText()).isEqualTo(syncLog.getId().toString());
        assertThat(payload.get("status").asText()).isEqualTo("IN_PROGRESS");
    }
}
