package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceTrend;
import java.util.List;
import java.util.Map;

public interface TrendAnalysisService {
    GovernanceTrend calculateTrend(String name, String module, List<Double> dataPoints, List<String> timestamps);
    GovernanceTrend getTrend(String name, String module);
    List<GovernanceTrend> getTrendsByModule(String module);
    Map<String, List<GovernanceTrend>> getAllTrends();
}
