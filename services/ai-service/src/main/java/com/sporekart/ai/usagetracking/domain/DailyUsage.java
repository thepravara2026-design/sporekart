package com.sporekart.ai.usagetracking.domain;

import java.util.List;

public record DailyUsage(
        String usageDate,
        List<UsageSummary> summaries,
        UsageSummary totals
) {
}
