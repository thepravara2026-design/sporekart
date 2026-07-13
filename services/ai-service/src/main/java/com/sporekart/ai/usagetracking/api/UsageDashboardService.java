package com.sporekart.ai.usagetracking.api;

import com.sporekart.ai.usagetracking.domain.UsageSummary;

import java.util.List;
import java.util.Map;

public interface UsageDashboardService {

    Map<String, Object> getDashboard();

    List<UsageSummary> getTopProviders();

    List<UsageSummary> getTopModels();

    Map<String, Double> getFailureRates();
}
