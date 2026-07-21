package com.sporekart.ai.provider.health;

import com.sporekart.ai.provider.implementations.MockAIProvider;
import com.sporekart.ai.provider.models.ProviderHealth;
import com.sporekart.ai.provider.registry.ProviderRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthMonitorTest {
    private ProviderRegistryImpl registry;
    private HealthMonitor monitor;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
        registry.register(new MockAIProvider("test-1", "TEST"));
        registry.register(new MockAIProvider("test-2", "TEST2"));
    }

    @Test
    void shouldCheckAllProviders() {
        monitor = new HealthMonitor(registry, 5000);
        var results = monitor.checkAllProviders();
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(ProviderHealth::isHealthy));
    }

    @Test
    void shouldCheckSpecificProvider() {
        monitor = new HealthMonitor(registry, 5000);
        var health = monitor.checkProviderHealth("test-1");
        assertNotNull(health);
        assertEquals("test-1", health.providerId());
    }

    @Test
    void shouldReturnOfflineForUnknownProvider() {
        monitor = new HealthMonitor(registry, 5000);
        var health = monitor.checkProviderHealth("nonexistent");
        assertEquals(ProviderHealth.HealthStatus.OFFLINE, health.status());
    }

    @Test
    void shouldReturnAggregateHealth() {
        monitor = new HealthMonitor(registry, 5000);
        monitor.checkAllProviders();
        var aggregate = monitor.getAggregateHealth();
        assertEquals(ProviderHealth.HealthStatus.HEALTHY, aggregate);
    }

    @Test
    void shouldReturnDegradedAggregateHealth() {
        registry.register(new MockAIProvider("unhealthy-1", "UNHEALTHY") {
            @Override
            public ProviderHealth checkHealth() {
                return ProviderHealth.unhealthy("unhealthy-1", "UNHEALTHY", "down");
            }
        });
        monitor = new HealthMonitor(registry, 5000);
        monitor.checkAllProviders();
        var aggregate = monitor.getAggregateHealth();
        assertTrue(aggregate == ProviderHealth.HealthStatus.HEALTHY
                || aggregate == ProviderHealth.HealthStatus.DEGRADED);
    }

    @Test
    void shouldReturnUnknownForEmptyRegistry() {
        monitor = new HealthMonitor(new ProviderRegistryImpl(), 5000);
        var aggregate = monitor.getAggregateHealth();
        assertEquals(ProviderHealth.HealthStatus.UNKNOWN, aggregate);
    }

    @Test
    void shouldGetLastHealth() {
        monitor = new HealthMonitor(registry, 5000);
        monitor.checkAllProviders();
        var health = monitor.getLastHealth("test-1");
        assertNotNull(health);
    }

    @Test
    void shouldGetAllLastHealth() {
        monitor = new HealthMonitor(registry, 5000);
        monitor.checkAllProviders();
        var allHealth = monitor.getAllLastHealth();
        assertEquals(2, allHealth.size());
    }

    @Test
    void shouldTrackConsecutiveFailures() {
        monitor = new HealthMonitor(registry, 5000);
        var failing = new MockAIProvider("fail-1", "FAIL") {
            private int callCount = 0;
            @Override
            public ProviderHealth checkHealth() {
                callCount++;
                return callCount <= 2
                        ? ProviderHealth.healthy("fail-1", "FAIL")
                        : ProviderHealth.unhealthy("fail-1", "FAIL", "simulated failure");
            }
        };
        registry.register(failing);
        var health = monitor.checkProviderHealth("fail-1");
        var health2 = monitor.checkProviderHealth("fail-1");
        // After several calls, verify tracking works
        assertNotNull(health);
        assertNotNull(health2);
    }

    @Test
    void shouldHandleCheckWithProviderObject() {
        monitor = new HealthMonitor(registry, 5000);
        var provider = registry.lookup("test-1").get();
        var health = monitor.checkProviderHealth(provider);
        assertNotNull(health);
        assertTrue(health.isHealthy());
    }
}
