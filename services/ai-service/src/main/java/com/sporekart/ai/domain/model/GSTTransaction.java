package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class GSTTransaction {
    private UUID id;
    private String transactionNumber;
    private String transactionType; // PURCHASE, SALE, CREDIT_NOTE, DEBIT_NOTE
    private LocalDate transactionDate;
    private String supplierGSTIN;
    private String buyerGSTIN;
    private BigDecimal totalAmount;
    private BigDecimal sgstRate;
    private BigDecimal cgstRate;
    private BigDecimal igstRate;
    private BigDecimal sgstAmount;
    private BigDecimal cgstAmount;
    private BigDecimal igstAmount;
    private BigDecimal totalTax;
    private BigDecimal grandTotal;
    private boolean itcEligible;
    private String referenceNumber;
    private String status; // PENDING, PROCESSED, FILED, REJECTED
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public GSTTransaction(UUID id, String transactionNumber, String transactionType,
            LocalDate transactionDate, BigDecimal totalAmount) {
        this.id = id;
        this.transactionNumber = transactionNumber;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.status = "PENDING";
        this.itcEligible = true;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getSgstAmount() {
        return sgstAmount != null ? sgstAmount : BigDecimal.ZERO;
    }

    public BigDecimal getCgstAmount() {
        return cgstAmount != null ? cgstAmount : BigDecimal.ZERO;
    }

    public BigDecimal getIgstAmount() {
        return igstAmount != null ? igstAmount : BigDecimal.ZERO;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public String getStatus() {
        return status;
    }

    public void calculateTax() {
        sgstAmount = sgstRate != null ? totalAmount.multiply(sgstRate).divide(new BigDecimal(100)) : BigDecimal.ZERO;
        cgstAmount = cgstRate != null ? totalAmount.multiply(cgstRate).divide(new BigDecimal(100)) : BigDecimal.ZERO;
        igstAmount = igstRate != null ? totalAmount.multiply(igstRate).divide(new BigDecimal(100)) : BigDecimal.ZERO;
        totalTax = sgstAmount.add(cgstAmount).add(igstAmount);
        grandTotal = totalAmount.add(totalTax);
        this.updatedAt = OffsetDateTime.now();
    }

    public void process() {
        this.status = "PROCESSED";
        this.updatedAt = OffsetDateTime.now();
    }

    public void file() {
        this.status = "FILED";
        this.updatedAt = OffsetDateTime.now();
    }
}
