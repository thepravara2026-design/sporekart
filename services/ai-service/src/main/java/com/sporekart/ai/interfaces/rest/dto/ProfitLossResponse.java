package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class ProfitLossResponse {
    @Schema(description = "Total revenue for the period", example = "3200.00")
    private BigDecimal revenue;

    @Schema(description = "Total expenses for the period", example = "1980.00")
    private BigDecimal expenses;

    @Schema(description = "Net profit for the period", example = "1220.00")
    private BigDecimal netProfit;

    @Schema(description = "Reporting period", example = "2026-07-01 to 2026-07-10")
    private String period;

    public ProfitLossResponse() {
    }

    public ProfitLossResponse(BigDecimal revenue, BigDecimal expenses, BigDecimal netProfit, String period) {
        this.revenue = revenue;
        this.expenses = expenses;
        this.netProfit = netProfit;
        this.period = period;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
