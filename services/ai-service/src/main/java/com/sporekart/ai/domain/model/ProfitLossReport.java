package com.sporekart.ai.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProfitLossReport {
    private BigDecimal revenue;
    private BigDecimal expenses;
    private BigDecimal netProfit;
    private String period;

    public ProfitLossReport() {
    }

    public ProfitLossReport(BigDecimal revenue, BigDecimal expenses, BigDecimal netProfit, String period) {
        this.revenue = revenue;
        this.expenses = expenses;
        this.netProfit = netProfit;
        this.period = period;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public String getPeriod() {
        return period;
    }
}
