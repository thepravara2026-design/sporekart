package com.sporekart.ai.usagetracking.domain;

import java.util.List;

public record MonthlyUsage(
        String yearMonth,
        List<UsageSummary> summaries,
        UsageSummary totals
) {
}
