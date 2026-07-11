package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Warehouse;
import com.sporekart.ai.domain.model.WarehouseStock;
import com.sporekart.ai.common.exception.WarehouseException;
import com.sporekart.ai.infrastructure.persistence.entity.WarehouseEntity;
import com.sporekart.ai.infrastructure.persistence.entity.WarehouseStockEntity;
import com.sporekart.ai.infrastructure.persistence.repository.WarehouseRepository;
import com.sporekart.ai.infrastructure.persistence.repository.WarehouseStockRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WarehouseService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final AuditService auditService;

    public WarehouseService(KafkaTemplate<String, String> kafkaTemplate, WarehouseRepository warehouseRepository,
            WarehouseStockRepository warehouseStockRepository, AuditService auditService) {
        this.kafkaTemplate = kafkaTemplate;
        this.warehouseRepository = warehouseRepository;
        this.warehouseStockRepository = warehouseStockRepository;
        this.auditService = auditService;
    }

    public Warehouse createWarehouse(String code, String name, String type, BigDecimal capacity)
            throws WarehouseException {
        if (capacity == null || capacity.compareTo(BigDecimal.ZERO) < 0) {
            throw new WarehouseException("Warehouse capacity must be zero or greater");
        }
        if (warehouseRepository.existsByWarehouseCode(code)) {
            throw new WarehouseException("Warehouse code already exists");
        }

        Warehouse warehouse = new Warehouse(UUID.randomUUID(), code, name, type, capacity);
        WarehouseEntity entity = new WarehouseEntity(warehouse.getId(), code, name, capacity, type);
        warehouseRepository.save(entity);
        auditService.logAction("warehouse", warehouse.getId().toString(), "CREATED", "Warehouse created: " + code);
        return warehouse;
    }

    public void addStock(UUID warehouseId, UUID productId, BigDecimal quantity, BigDecimal reorderLevel)
            throws WarehouseException {
        Optional<WarehouseEntity> maybe = warehouseRepository.findById(warehouseId);
        if (maybe.isEmpty())
            throw new WarehouseException("Warehouse not found");

        UUID stockId = UUID.randomUUID();
        WarehouseStockEntity ws = new WarehouseStockEntity(stockId, warehouseId, productId, quantity, quantity,
                reorderLevel, BigDecimal.ZERO);
        warehouseStockRepository.save(ws);
    }

    public void transferStock(UUID fromWarehouseId, UUID toWarehouseId, UUID productId, BigDecimal quantity)
            throws WarehouseException {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WarehouseException("Transfer quantity must be greater than zero");
        }

        WarehouseStockEntity fromStock = null;
        for (WarehouseStockEntity e : warehouseStockRepository.findAll()) {
            if (e.getWarehouseId().equals(fromWarehouseId) && e.getProductId().equals(productId)) {
                fromStock = e;
                break;
            }
        }
        if (fromStock == null)
            throw new WarehouseException("Product not in source warehouse");
        if (fromStock.getQuantityAvailable().compareTo(quantity) < 0) {
            throw new WarehouseException("Insufficient stock for transfer");
        }

        fromStock.setQuantityAvailable(fromStock.getQuantityAvailable().subtract(quantity));
        warehouseStockRepository.save(fromStock);
        addStock(toWarehouseId, productId, quantity, fromStock.getReorderLevel());
        auditService.logAction("warehouse_transfer", fromWarehouseId + "->" + toWarehouseId, "TRANSFERRED",
                "Transferred " + quantity + " units");
        publishEvent("WarehouseTransferCompleted", fromWarehouseId + "->" + toWarehouseId);
    }

    public Map<String, Object> getStockLevel(UUID warehouseId) throws WarehouseException {
        Optional<WarehouseEntity> maybe = warehouseRepository.findById(warehouseId);
        if (maybe.isEmpty())
            throw new WarehouseException("Warehouse not found");

        Map<String, Object> result = new HashMap<>();
        BigDecimal totalQuantity = BigDecimal.ZERO;
        List<Map<String, Object>> items = new ArrayList<>();

        for (WarehouseStockEntity ws : warehouseStockRepository.findAll()) {
            if (ws.getWarehouseId().equals(warehouseId)) {
                totalQuantity = totalQuantity.add(ws.getQuantityOnHand());
                Map<String, Object> item = new HashMap<>();
                item.put("productId", ws.getProductId());
                item.put("quantity", ws.getQuantityOnHand());
                item.put("needsReorder", ws.getQuantityOnHand().compareTo(ws.getReorderLevel()) <= 0);
                items.add(item);
            }
        }

        result.put("warehouseId", warehouseId);
        result.put("totalQuantity", totalQuantity);
        result.put("items", items);
        return result;
    }

    public List<Warehouse> getWarehouses() {
        List<Warehouse> result = new ArrayList<>();
        for (WarehouseEntity e : warehouseRepository.findAll()) {
            result.add(new Warehouse(e.getId(), e.getWarehouseCode(), e.getWarehouseName(), e.getWarehouseType(),
                    e.getCapacityUnits()));
        }
        return result;
    }

    private WarehouseStockEntity findStock(UUID warehouseId, UUID productId) {
        for (WarehouseStockEntity ws : warehouseStockRepository.findAll()) {
            if (ws.getWarehouseId().equals(warehouseId) && ws.getProductId().equals(productId)) {
                return ws;
            }
        }
        return null;
    }

    private void publishEvent(String eventType, String eventData) {
        try {
            kafkaTemplate.send("warehouse-events", eventType, eventData);
        } catch (Exception e) {
            System.out.println("Failed to publish warehouse event: " + e.getMessage());
        }
    }
}
