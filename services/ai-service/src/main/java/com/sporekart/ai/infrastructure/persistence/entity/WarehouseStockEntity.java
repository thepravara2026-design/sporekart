package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouse_stock")
public class WarehouseStockEntity {
    @Id
    private UUID id;

    @Column(name = "warehouse_id", nullable = false)
    private UUID warehouseId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity_on_hand")
    private BigDecimal quantityOnHand;

    @Column(name = "quantity_available")
    private BigDecimal quantityAvailable;

    @Column(name = "quantity_reserved")
    private BigDecimal quantityReserved;

    @Column(name = "reorder_level")
    private BigDecimal reorderLevel;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public WarehouseStockEntity() {
    }

    public WarehouseStockEntity(UUID id, UUID warehouseId, UUID productId, BigDecimal quantityOnHand,
            BigDecimal quantityAvailable, BigDecimal reorderLevel, BigDecimal unitCost) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.quantityOnHand = quantityOnHand;
        this.quantityAvailable = quantityAvailable;
        this.quantityReserved = BigDecimal.ZERO;
        this.reorderLevel = reorderLevel;
        this.unitCost = unitCost;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
        this.isDeleted = false;
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

    public BigDecimal getReorderLevel() {
        return reorderLevel;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setQuantityAvailable(BigDecimal q) {
        this.quantityAvailable = q;
        this.updatedAt = OffsetDateTime.now();
    }

    public void setReorderLevel(BigDecimal r) {
        this.reorderLevel = r;
        this.updatedAt = OffsetDateTime.now();
    }
}
