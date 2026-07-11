package com.sporekart.ai.infrastructure.persistence.repository;

import com.sporekart.ai.infrastructure.persistence.entity.WarehouseStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseStockRepository extends JpaRepository<WarehouseStockEntity, UUID> {
}
