package com.sporekart.inventory.application.service;

import com.sporekart.inventory.domain.model.InventoryItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class InventoryServiceTest {
    @Test
    void snapshotReturnsInventoryItem() {
        InventoryService service = new InventoryService();
        InventoryItem item = service.snapshot("product-1");
        assertNotNull(item);
    }
}
