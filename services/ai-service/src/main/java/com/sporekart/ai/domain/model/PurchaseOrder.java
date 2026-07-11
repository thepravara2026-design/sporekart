package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PurchaseOrder {
    private UUID id;
    private String poNumber;
    private UUID supplierId;
    private LocalDate poDate;
    private LocalDate deliveryDate;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal grandTotal;
    private String status; // DRAFT, SUBMITTED, APPROVED, REJECTED, RECEIVED, CANCELLED
    private UUID approvedBy;
    private OffsetDateTime approvedAt;
    private String paymentTerms;
    private String notes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public PurchaseOrder(UUID id, String poNumber, UUID supplierId, LocalDate poDate,
            BigDecimal totalAmount, BigDecimal taxAmount) {
        this.id = id;
        this.poNumber = poNumber;
        this.supplierId = supplierId;
        this.poDate = poDate;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.grandTotal = totalAmount.add(taxAmount);
        this.status = "DRAFT";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public LocalDate getPoDate() {
        return poDate;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public String getStatus() {
        return status;
    }

    public void submit() {
        this.status = "SUBMITTED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void approve(UUID userId) {
        this.status = "APPROVED";
        this.approvedBy = userId;
        this.approvedAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void reject() {
        this.status = "REJECTED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void markReceived() {
        this.status = "RECEIVED";
        this.updatedAt = OffsetDateTime.now();
    }
}
