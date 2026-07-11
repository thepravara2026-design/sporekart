package com.sporekart.ai.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntryEntity {
    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "journal_id", nullable = false)
    private UUID journalId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public LedgerEntryEntity() {
    }

    public LedgerEntryEntity(UUID id, UUID accountId, UUID journalId, LocalDate entryDate, BigDecimal debitAmount,
            BigDecimal creditAmount, String description) {
        this.id = id;
        this.accountId = accountId;
        this.journalId = journalId;
        this.entryDate = entryDate;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.description = description;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getJournalId() {
        return journalId;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public String getDescription() {
        return description;
    }

}
