package com.sporekart.ai.application.service;

import com.sporekart.ai.common.exception.SynchronizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class InventorySyncServiceTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private InventorySyncService inventorySyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventorySyncService = new InventorySyncService(kafkaTemplate);
    }

    @Test
    void shouldSyncInventorySuccessfully() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("itemId", "SKU-001");
        payload.put("quantity", 120);

        inventorySyncService.syncInventory("ERP", "WMS", payload);

        Map<String, Object> status = inventorySyncService.getSyncStatus();

        assertThat(status).containsEntry("pendingItems", 0);
        assertThat(status).containsEntry("totalRetries", 0);
    }

    @Test
    void shouldRejectInvalidSyncPayload() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("itemId", "SKU-002");
        payload.put("quantity", 0);

        assertThatThrownBy(() -> inventorySyncService.syncInventory("ERP", "WMS", payload))
                .isInstanceOf(SynchronizationException.class)
                .hasMessageContaining("quantity must be greater than zero");
    }

    @Test
    void shouldNotFailWhenEventPublishFails() {
        doThrow(new RuntimeException("kafka down")).when(kafkaTemplate).send("inventory-sync-events", "SyncProcessing",
                "ERP->WMS");

        Map<String, Object> payload = new HashMap<>();
        payload.put("itemId", "SKU-003");
        payload.put("quantity", 50);

        inventorySyncService.syncInventory("ERP", "WMS", payload);

        verify(kafkaTemplate).send("inventory-sync-events", "SyncProcessing", "ERP->WMS");
        Map<String, Object> status = inventorySyncService.getSyncStatus();
        assertThat(status).containsEntry("pendingItems", 0);
    }
}
