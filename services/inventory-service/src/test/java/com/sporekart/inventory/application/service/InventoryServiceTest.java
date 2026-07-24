package com.sporekart.inventory.application.service;

import com.sporekart.inventory.domain.model.InventoryItem;
import com.sporekart.inventory.domain.repository.InventoryRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryServiceTest {
    @Test
    void snapshotReturnsInventoryItem() {
        InventoryRepositoryPort repositoryPort = mock(InventoryRepositoryPort.class);
        when(repositoryPort.findById(anyString())).thenReturn(
                Optional.of(new InventoryItem("product-1", 100, 10, 90)));
        InventoryService service = new InventoryService(repositoryPort);
        InventoryItem item = service.snapshot("product-1");
        assertNotNull(item);
    }
}
