package com.sporekart.inventory.domain.repository;

import com.sporekart.inventory.domain.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepositoryPort {
    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findById(String id);

    List<InventoryItem> findAll();
}
