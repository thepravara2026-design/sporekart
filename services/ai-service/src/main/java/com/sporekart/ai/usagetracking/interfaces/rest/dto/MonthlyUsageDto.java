package com.sporekart.ai.usagetracking.interfaces.rest.dto;

import com.sporekart.ai.usagetracking.domain.UsageSummary;

import java.util.List;

public class MonthlyUsageDto {

    private String yearMonth;
    private List<UsageSummary> summaries;
    private UsageSummary totals;

    public MonthlyUsageDto() {
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<UsageSummary> getSummaries() {
        return summaries;
    }

    public void setSummaries(List<UsageSummary> summaries) {
        this.summaries = summaries;
    }

    public UsageSummary getTotals() {
        return totals;
    }

    public void setTotals(UsageSummary totals) {
        this.totals = totals;
    }
}
