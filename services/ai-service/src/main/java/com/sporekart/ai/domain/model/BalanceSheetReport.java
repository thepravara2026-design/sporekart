package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceSheetReport {
    private BigDecimal assets;
    private BigDecimal liabilities;
    private BigDecimal equity;
    private LocalDate reportDate;

    public BalanceSheetReport() {
    }

    public BalanceSheetReport(BigDecimal assets, BigDecimal liabilities, BigDecimal equity, LocalDate reportDate) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.equity = equity;
        this.reportDate = reportDate;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public BigDecimal getLiabilities() {
        return liabilities;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }
}
