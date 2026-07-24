package com.sporekart.inventory.infrastructure.persistence;

import com.sporekart.inventory.domain.model.InventoryItem;
import com.sporekart.inventory.domain.repository.InventoryRepositoryPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JpaInventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final InventoryJpaRepository jpaRepository;

    public JpaInventoryRepositoryAdapter(InventoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        InventoryItemEntity entity = InventoryItemEntity.fromDomain(item);
        InventoryItemEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        return jpaRepository.findById(id).map(InventoryItemEntity::toDomain);
    }

    @Override
    public List<InventoryItem> findAll() {
        return jpaRepository.findAll().stream()
                .map(InventoryItemEntity::toDomain)
                .toList();
    }
}
