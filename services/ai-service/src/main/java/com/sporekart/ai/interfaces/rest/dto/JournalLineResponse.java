package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

public class JournalLineResponse {
    @Schema(description = "Journal identifier", example = "d94a1f1f-6c9f-4ab8-84c1-115a7a6f5b7c")
    private UUID journalId;

    @Schema(description = "Total debit amount after adding the line", example = "1250.00")
    private BigDecimal totalDebit;

    @Schema(description = "Total credit amount after adding the line", example = "1250.00")
    private BigDecimal totalCredit;

    public JournalLineResponse() {
    }

    public JournalLineResponse(UUID journalId, BigDecimal totalDebit, BigDecimal totalCredit) {
        this.journalId = journalId;
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
    }

    public UUID getJournalId() {
        return journalId;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setJournalId(UUID journalId) {
        this.journalId = journalId;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
}
