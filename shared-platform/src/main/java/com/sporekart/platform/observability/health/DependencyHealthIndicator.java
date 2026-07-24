package com.sporekart.platform.observability.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class DependencyHealthIndicator implements HealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(DependencyHealthIndicator.class);
    private final Map<String, Supplier<Health>> dependencyChecks = new LinkedHashMap<>();

    public void registerDependency(String name, Supplier<Health> check) {
        dependencyChecks.put(name, check);
        log.debug("Dependency registered: {}", name);
    }

    @Override
    public Health health() {
        Map<String, Object> details = new LinkedHashMap<>();
        boolean allUp = true;

        for (Map.Entry<String, Supplier<Health>> entry : dependencyChecks.entrySet()) {
            try {
                Health depHealth = entry.getValue().get();
                details.put(entry.getKey(), depHealth.getStatus().getCode());
                if (!depHealth.getStatus().equals(Health.up().build().getStatus())) {
                    allUp = false;
                    details.put(entry.getKey() + "_detail", depHealth.getDetails());
                }
            } catch (Exception e) {
                allUp = false;
                details.put(entry.getKey(), "DOWN");
                details.put(entry.getKey() + "_error", e.getMessage());
                log.warn("Dependency health check failed: {}", entry.getKey(), e);
            }
        }

        Health.Builder builder = allUp ? Health.up() : Health.down();
        builder.withDetail("dependencies", details);
        builder.withDetail("total_dependencies", dependencyChecks.size());
        builder.withDetail("healthy_dependencies",
            details.entrySet().stream()
                .filter(e -> !e.getKey().endsWith("_detail") && !e.getKey().endsWith("_error"))
                .filter(e -> "UP".equals(e.getValue()))
                .count());

        return builder.build();
    }
}
