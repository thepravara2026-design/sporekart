package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceSheetResponse {
    @Schema(description = "Total assets", example = "5000.00")
    private BigDecimal assets;

    @Schema(description = "Total liabilities", example = "2100.00")
    private BigDecimal liabilities;

    @Schema(description = "Total equity", example = "2900.00")
    private BigDecimal equity;

    @Schema(description = "Balance sheet report date", example = "2026-07-10")
    private LocalDate reportDate;

    public BalanceSheetResponse() {
    }

    public BalanceSheetResponse(BigDecimal assets, BigDecimal liabilities, BigDecimal equity, LocalDate reportDate) {
        this.assets = assets;
        this.liabilities = liabilities;
        this.equity = equity;
        this.reportDate = reportDate;
    }

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    public BigDecimal getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(BigDecimal liabilities) {
        this.liabilities = liabilities;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
}
