package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class LedgerEntry {
    private UUID id;
    private UUID accountId;
    private UUID journalEntryId;
    private LocalDate entryDate;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public LedgerEntry(UUID id, UUID accountId, UUID journalEntryId, LocalDate entryDate,
            BigDecimal debitAmount, BigDecimal creditAmount, String description) {
        this.id = id;
        this.accountId = accountId;
        this.journalEntryId = journalEntryId;
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

    public UUID getJournalEntryId() {
        return journalEntryId;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
}
