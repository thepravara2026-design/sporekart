package com.sporekart.ai.gateway.observability;

import java.util.Map;

public interface MonitoringDashboard {
    Map<String, Object> getDashboardSummary();
    Map<String, Object> getPipelineMetrics();
    Map<String, Object> getProviderMetrics();
    Map<String, Object> getErrorMetrics();
    Map<String, Object> getLatencyMetrics();
    Map<String, Object> getCustomMetrics(String metricGroup);
}
