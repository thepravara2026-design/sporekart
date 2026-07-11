package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class WarehouseStock {
    private UUID id;
    private UUID warehouseId;
    private UUID productId;
    private BigDecimal quantityOnHand;
    private BigDecimal quantityAvailable;
    private BigDecimal quantityReserved;
    private BigDecimal reorderLevel;
    private BigDecimal reorderQuantity;
    private BigDecimal unitCost;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public WarehouseStock(UUID id, UUID warehouseId, UUID productId,
            BigDecimal quantityOnHand, BigDecimal reorderLevel) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.quantityAvailable = quantityOnHand;
        this.quantityReserved = BigDecimal.ZERO;
        this.reorderLevel = reorderLevel;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public UUID getProductId() {
        return productId;
    }

    public BigDecimal getQuantityOnHand() {
        return quantityOnHand;
    }

    public BigDecimal getQuantityAvailable() {
        return quantityAvailable;
    }

    public BigDecimal getQuantityReserved() {
        return quantityReserved;
    }

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }

    public void addStock(BigDecimal quantity) {
        this.quantityOnHand = quantityOnHand.add(quantity);
        this.quantityAvailable = quantityAvailable.add(quantity);
        this.updatedAt = OffsetDateTime.now();
    }

    public void removeStock(BigDecimal quantity) {
        this.quantityOnHand = quantityOnHand.subtract(quantity);
        this.quantityAvailable = quantityAvailable.subtract(quantity);
        this.updatedAt = OffsetDateTime.now();
    }

    public void reserve(BigDecimal quantity) {
        this.quantityAvailable = quantityAvailable.subtract(quantity);
        this.quantityReserved = quantityReserved.add(quantity);
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean needsReorder() {
        return quantityOnHand.compareTo(reorderLevel) <= 0;
    }
}
