package com.sporekart.ai.usagetracking.interfaces.rest.dto;

import com.sporekart.ai.usagetracking.domain.UsageSummary;

import java.util.List;

public class DailyUsageDto {

    private String usageDate;
    private List<UsageSummary> summaries;
    private UsageSummary totals;

    public DailyUsageDto() {
    }

    public String getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(String usageDate) {
        this.usageDate = usageDate;
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
