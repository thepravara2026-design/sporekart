package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Supplier {
    private UUID id;
    private String supplierCode;
    private String supplierName;
    private String supplierType; // MANUFACTURER, DISTRIBUTOR, RETAILER, SERVICE_PROVIDER
    private String gstin;
    private String pan;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String paymentTerms;
    private BigDecimal creditLimit;
    private BigDecimal currentBalance;
    private String status; // PENDING, APPROVED, BLACKLISTED, INACTIVE
    private BigDecimal rating;
    private UUID approvedBy;
    private OffsetDateTime approvedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public Supplier(UUID id, String supplierCode, String supplierName, String supplierType,
            String gstin, String email) {
        this.id = id;
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.supplierType = supplierType;
        this.gstin = gstin;
        this.email = email;
        this.status = "PENDING";
        this.rating = BigDecimal.ZERO;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
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

    public String getStatus() {
        return status;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void approve(UUID userId) {
        this.status = "APPROVED";
        this.approvedBy = userId;
        this.approvedAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void blacklist() {
        this.status = "BLACKLISTED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateRating(BigDecimal newRating) {
        this.rating = newRating;
        this.updatedAt = OffsetDateTime.now();
    }

    public void updateBalance(BigDecimal amount) {
        this.currentBalance = currentBalance.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }
}
