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
@Table(name = "journal_entries")
public class JournalEntryEntity {
    @Id
    private UUID id;

    @Column(name = "journal_number", nullable = false, unique = true)
    private String journalNumber;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "total_debit")
    private BigDecimal totalDebit;

    @Column(name = "total_credit")
    private BigDecimal totalCredit;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public JournalEntryEntity() {
    }

    public JournalEntryEntity(UUID id, String journalNumber, LocalDate entryDate, String description,
            String referenceType, UUID referenceId, BigDecimal totalDebit, BigDecimal totalCredit) {
        this.id = id;
        this.journalNumber = journalNumber;
        this.entryDate = entryDate;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.status = "DRAFT";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public String getDescription() {
        return description;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
