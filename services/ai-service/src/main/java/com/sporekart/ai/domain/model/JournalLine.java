package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class JournalLine {
    private UUID accountId;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String description;

    public JournalLine(UUID accountId, BigDecimal debitAmount, BigDecimal creditAmount, String description) {
        this.accountId = accountId;
        this.debitAmount = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        this.creditAmount = creditAmount != null ? creditAmount : BigDecimal.ZERO;
        this.description = description;
    }

    public UUID getAccountId() {
        return accountId;
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
