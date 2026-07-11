package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
public class SupplierEntity {
    @Id
    private UUID id;

    @Column(name = "supplier_code", nullable = false, unique = true)
    private String supplierCode;

    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "gstin", unique = true)
    private String gstin;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "supplier_type", nullable = false)
    private String supplierType;

    @Column(name = "rating")
    private BigDecimal rating;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public SupplierEntity() {
    }

    public SupplierEntity(UUID id, String supplierCode, String supplierName, String gstin, String email) {
        this(id, supplierCode, supplierName, gstin, email, "DISTRIBUTOR");
    }

    public SupplierEntity(UUID id, String supplierCode, String supplierName, String gstin, String email,
            String supplierType) {
        this.id = id;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.gstin = gstin;
        this.email = email;
        this.status = "PENDING";
        this.supplierType = supplierType != null ? supplierType : "DISTRIBUTOR";
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getGstin() {
        return gstin;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

}
