package com.sporekart.ai.gateway.health;

import com.sporekart.ai.gateway.domain.HealthStatus;

import java.util.List;
import java.util.Map;

public interface GatewayHealthIndicator {
    HealthStatus check(String component);
    Map<String, HealthStatus> checkAll();
    List<String> getComponents();
    boolean isHealthy(String component);
    boolean isOverallHealthy();
    void registerComponent(String name, HealthChecker checker);

    interface HealthChecker {
        HealthStatus check();
    }
}
