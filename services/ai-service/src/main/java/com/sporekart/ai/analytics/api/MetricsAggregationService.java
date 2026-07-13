package com.sporekart.ai.analytics.api;

import com.sporekart.ai.analytics.domain.GovernanceMetric;
import com.sporekart.ai.analytics.domain.MetricType;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public interface MetricsAggregationService {
    GovernanceMetric recordMetric(String name, String module, MetricType type, double value, Map<String, String> labels);
    Map<String, Object> getAggregatedMetrics(String module, String metricName, Instant from, Instant to);
    List<GovernanceMetric> getMetricsByModule(String module);
    Map<String, Object> getMetricsSummary();
}
