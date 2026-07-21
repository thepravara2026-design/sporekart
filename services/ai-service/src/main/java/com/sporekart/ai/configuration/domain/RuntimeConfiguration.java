package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record RuntimeConfiguration(
    boolean enabled,
    int maxAgentExecutions,
    int maxExecutionDurationSeconds,
    boolean dynamicModelSwitching,
    boolean providerPriorityEnabled,
    boolean trafficRoutingEnabled,
    boolean canaryRolloutEnabled,
    double canaryTrafficPercent,
    boolean emergencyDisableEnabled,
    boolean maintenanceMode,
    Duration requestTimeout,
    int circuitBreakerThreshold,
    Duration circuitBreakerResetTimeout,
    int maxRetries,
    Duration retryBackoff,
    int maxConcurrency,
    int batchSize,
    boolean streamingMode,
    boolean hotReloadEnabled,
    Duration hotReloadInterval
) {
    public static RuntimeConfiguration defaults() {
        return new RuntimeConfiguration(
            true, 1000, 300,
            true, true, false,
            false, 10.0,
            true, false,
            Duration.ofSeconds(60), 5, Duration.ofSeconds(30),
            3, Duration.ofSeconds(1),
            50, 10,
            true, true, Duration.ofSeconds(30)
        );
    }
}
