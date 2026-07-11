package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TrialBalanceResponse {
    @Schema(description = "Total debit amount", example = "1250.00")
    private BigDecimal totalDebit;

    @Schema(description = "Total credit amount", example = "1250.00")
    private BigDecimal totalCredit;

    @Schema(description = "Indicates whether the trial balance is balanced", example = "true")
    private boolean isBalanced;

    @Schema(description = "Balance report date", example = "2026-07-10")
    private LocalDate reportDate;

    public TrialBalanceResponse() {
    }

    public TrialBalanceResponse(BigDecimal totalDebit, BigDecimal totalCredit, boolean isBalanced,
            LocalDate reportDate) {
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.isBalanced = isBalanced;
        this.reportDate = reportDate;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public boolean isBalanced() {
        return isBalanced;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public void setBalanced(boolean balanced) {
        isBalanced = balanced;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
