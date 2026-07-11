package com.sporekart.inventory.infrastructure.persistence;

import com.sporekart.inventory.domain.model.InventoryItem;
import com.sporekart.inventory.domain.repository.InventoryRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryInventoryRepository implements InventoryRepositoryPort {
    private final Map<String, InventoryItem> itemsById = new ConcurrentHashMap<>();

    @Override
    public InventoryItem save(InventoryItem item) {
        itemsById.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        return Optional.ofNullable(itemsById.get(id));
    }

    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(itemsById.values());
    }
}
