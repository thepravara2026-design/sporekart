package com.sporekart.inventory.application.service;

import com.sporekart.inventory.domain.model.InventoryItem;
import com.sporekart.inventory.domain.repository.InventoryRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepositoryPort repositoryPort;

    public InventoryService(InventoryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public InventoryItem snapshot(String productId) {
        return repositoryPort.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
    }
}
