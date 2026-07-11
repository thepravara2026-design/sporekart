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
@Table(name = "gst_transactions")
public class GSTTransactionEntity {
    @Id
    private UUID id;

    @Column(name = "transaction_number", nullable = false, unique = true)
    private String transactionNumber;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "supplier_gstin")
    private String supplierGstin;

    @Column(name = "buyer_gstin")
    private String buyerGstin;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "sgst_rate")
    private BigDecimal sgstRate;

    @Column(name = "cgst_rate")
    private BigDecimal cgstRate;

    @Column(name = "igst_rate")
    private BigDecimal igstRate;

    @Column(name = "sgst_amount")
    private BigDecimal sgstAmount;

    @Column(name = "cgst_amount")
    private BigDecimal cgstAmount;

    @Column(name = "igst_amount")
    private BigDecimal igstAmount;

    @Column(name = "total_tax")
    private BigDecimal totalTax;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public GSTTransactionEntity() {
    }

    public GSTTransactionEntity(UUID id, String transactionNumber, String transactionType, LocalDate transactionDate,
            BigDecimal totalAmount) {
        this.id = id;
        this.transactionNumber = transactionNumber;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.status = "PENDING";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }
}
