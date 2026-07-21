package com.sporekart.ai.providers.config;

import com.sporekart.ai.providers.ProviderHealthService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ProviderHealthIndicator implements HealthIndicator {

    private final ProviderHealthService healthService;

    public ProviderHealthIndicator(ProviderHealthService healthService) {
        this.healthService = healthService;
    }

    @Override
    public Health health() {
        var allHealth = healthService.checkAll();
        var healthyCount = allHealth.values().stream().filter(h -> h.available()).count();
        var totalCount = allHealth.size();

        if (healthyCount == totalCount) {
            return Health.up()
                .withDetail("totalProviders", totalCount)
                .withDetail("healthyProviders", healthyCount)
                .withDetail("unhealthyProviders", totalCount - healthyCount)
                .build();
        }

        return Health.status("DEGRADED")
            .withDetail("totalProviders", totalCount)
            .withDetail("healthyProviders", healthyCount)
            .withDetail("unhealthyProviders", totalCount - healthyCount)
            .build();
    }
}
