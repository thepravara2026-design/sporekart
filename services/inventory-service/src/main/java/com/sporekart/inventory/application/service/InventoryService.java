package com.sporekart.inventory.application.service;

import com.sporekart.inventory.domain.model.InventoryItem;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    public InventoryItem snapshot(String productId) {
        return new InventoryItem(productId, 100, 10, 90);
    }
}
