package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JournalEntry {
    private UUID id;
    private String journalNumber;
    private LocalDate entryDate;
    private String description;
    private String referenceType;
    private UUID referenceId;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private List<JournalLine> lines = new ArrayList<>();
    private String status; // DRAFT, POSTED, REJECTED
    private UUID postedBy;
    private OffsetDateTime postedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public JournalEntry(UUID id, String journalNumber, LocalDate entryDate, String description,
            String referenceType, UUID referenceId) {
        this.id = id;
        this.journalNumber = journalNumber;
        this.entryDate = entryDate;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.totalDebit = BigDecimal.ZERO;
        this.totalCredit = BigDecimal.ZERO;
        this.status = "DRAFT";
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public List<JournalLine> getLines() {
        return lines;
    }

    public void addLine(JournalLine line) {
        this.lines.add(line);
        if (line.getDebitAmount() != null)
            this.totalDebit = this.totalDebit.add(line.getDebitAmount());
        if (line.getCreditAmount() != null)
            this.totalCredit = this.totalCredit.add(line.getCreditAmount());
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

    public String getStatus() {
        return status;
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

    public void addDebit(BigDecimal amount) {
        this.totalDebit = totalDebit.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }

    public void addCredit(BigDecimal amount) {
        this.totalCredit = totalCredit.add(amount);
        this.updatedAt = OffsetDateTime.now();
    }

    public void post(UUID userId) {
        this.status = "POSTED";
        this.postedBy = userId;
        this.postedAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void reject() {
        this.status = "REJECTED";
        this.updatedAt = OffsetDateTime.now();
    }

    public boolean isBalanced() {
        return totalDebit.compareTo(totalCredit) == 0;
    }
}
