package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Warehouse {
    private UUID id;
    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private BigDecimal capacityUnits;
    private String warehouseType; // DISTRIBUTION, REGIONAL, LOCAL, COLD_STORAGE
    private String status; // ACTIVE, INACTIVE, MAINTENANCE, CLOSED
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public Warehouse(UUID id, String warehouseCode, String warehouseName,
            String warehouseType, BigDecimal capacityUnits) {
        this.id = id;
        this.warehouseCode = warehouseCode;
        this.warehouseName = warehouseName;
        this.warehouseType = warehouseType;
        this.capacityUnits = capacityUnits;
        this.status = "ACTIVE";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
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

    public String getWarehouseType() {
        return warehouseType;
    }

    public BigDecimal getCapacityUnits() {
        return capacityUnits;
    }

    public String getStatus() {
        return status;
    }

    public void deactivate() {
        this.status = "INACTIVE";
        this.updatedAt = OffsetDateTime.now();
    }

    public void setMaintenance() {
        this.status = "MAINTENANCE";
        this.updatedAt = OffsetDateTime.now();
    }

    public void activate() {
        this.status = "ACTIVE";
        this.updatedAt = OffsetDateTime.now();
    }
}
