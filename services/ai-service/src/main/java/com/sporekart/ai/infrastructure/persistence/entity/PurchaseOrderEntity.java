package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrderEntity {
    @Id
    private UUID id;

    @Column(name = "po_number", nullable = false, unique = true)
    private String poNumber;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId;

    @Column(name = "po_date")
    private LocalDate poDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public PurchaseOrderEntity() {
    }

    public PurchaseOrderEntity(UUID id, String poNumber, UUID supplierId, LocalDate poDate, BigDecimal totalAmount,
            BigDecimal taxAmount) {
        this.id = id;
        this.poNumber = poNumber;
        this.supplierId = supplierId;
        this.poDate = poDate;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.grandTotal = totalAmount.add(taxAmount != null ? taxAmount : BigDecimal.ZERO);
        this.status = "DRAFT";
        this.createdAt = OffsetDateTime.now();
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
