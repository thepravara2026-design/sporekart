package com.sporekart.ai.usagetracking.api;

import com.sporekart.ai.usagetracking.domain.DailyUsage;
import com.sporekart.ai.usagetracking.domain.MonthlyUsage;
import com.sporekart.ai.usagetracking.domain.UsageSummary;

import java.util.List;

public interface UsageAggregationService {

    DailyUsage getDailyUsage(String date);

    MonthlyUsage getMonthlyUsage(String yearMonth);

    List<UsageSummary> getByProvider(String providerId);

    List<UsageSummary> getByModel(String modelId);
}
