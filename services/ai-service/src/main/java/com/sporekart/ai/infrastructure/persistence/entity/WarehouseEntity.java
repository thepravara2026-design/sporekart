package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "warehouses")
public class WarehouseEntity {
    @Id
    private UUID id;

    @Column(name = "warehouse_code", nullable = false, unique = true)
    private String warehouseCode;

    @Column(name = "warehouse_name", nullable = false)
    private String warehouseName;

    @Column(name = "capacity_units")
    private BigDecimal capacityUnits;

    @Column(name = "warehouse_type")
    private String warehouseType;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public WarehouseEntity() {
    }

    public WarehouseEntity(UUID id, String warehouseCode, String warehouseName, BigDecimal capacityUnits,
            String warehouseType) {
        this.id = id;
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
        this.capacityUnits = capacityUnits;
        this.warehouseType = warehouseType;
        this.status = "ACTIVE";
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public BigDecimal getCapacityUnits() {
        return capacityUnits;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
