package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TrialBalanceReport {
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private boolean balanced;
    private LocalDate reportDate;

    public TrialBalanceReport() {
    }

    public TrialBalanceReport(BigDecimal totalDebit, BigDecimal totalCredit, boolean balanced, LocalDate reportDate) {
        this.totalDebit = totalDebit;
        this.totalCredit = totalCredit;
        this.balanced = balanced;
        this.reportDate = reportDate;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }
}
