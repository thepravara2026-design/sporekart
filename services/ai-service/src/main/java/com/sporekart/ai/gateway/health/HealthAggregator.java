package com.sporekart.ai.gateway.health;

import java.util.List;
import java.util.Map;

public interface HealthAggregator {
    HealthCheckResult aggregate(List<HealthCheckResult> results);
    HealthCheckResult aggregate(Map<String, HealthCheckResult> componentResults);
    String determineOverallStatus(List<HealthCheckResult> results);
}
