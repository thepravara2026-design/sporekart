package com.sporekart.ai.providers;

import com.sporekart.ai.providers.audit.AuditEvent;
import com.sporekart.ai.providers.audit.AuditEventType;
import com.sporekart.ai.providers.availability.AvailabilitySnapshot;
import com.sporekart.ai.providers.circuit.CircuitState;
import com.sporekart.ai.providers.circuit.breaker.CircuitBreakerConfig;
import com.sporekart.ai.providers.health.HealthConfiguration;
import com.sporekart.ai.providers.health.HealthResponse;
import com.sporekart.ai.providers.health.HealthSnapshot;
import com.sporekart.ai.providers.health.HealthStatus;
import com.sporekart.ai.providers.health.liveness.LivenessState;
import com.sporekart.ai.providers.health.readiness.ReadinessState;
import com.sporekart.ai.providers.heartbeat.HeartbeatConfiguration;
import com.sporekart.ai.providers.heartbeat.HeartbeatTimeout;
import com.sporekart.ai.providers.lifecycle.LifecycleSnapshot;
import com.sporekart.ai.providers.lifecycle.state.LifecycleState;
import com.sporekart.ai.providers.lifecycle.state.StateTransition;
import com.sporekart.ai.providers.lifecycle.transition.LifecycleTransition;
import com.sporekart.ai.providers.maintenance.MaintenanceState;
import com.sporekart.ai.providers.maintenance.MaintenanceWindow;
import com.sporekart.ai.providers.metrics.MetricsModel;
import com.sporekart.ai.providers.recovery.RecoveryContext;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProviderLifecycleHealthTest {

    @Test
    void shouldHaveAllLifecycleStates() {
        assertEquals(15, LifecycleState.values().length);
        assertTrue(LifecycleTransition.isAllowed(LifecycleState.UNKNOWN, LifecycleState.REGISTERED));
        assertTrue(LifecycleTransition.isAllowed(LifecycleState.ACTIVE, LifecycleState.DEGRADED));
        assertTrue(LifecycleTransition.isAllowed(LifecycleState.READY, LifecycleState.ACTIVE));
        assertFalse(LifecycleTransition.isAllowed(LifecycleState.REMOVED, LifecycleState.ACTIVE));
        assertFalse(LifecycleTransition.isAllowed(LifecycleState.UNKNOWN, LifecycleState.ACTIVE));
    }

    @Test
    void shouldCreateStateTransition() {
        var t = StateTransition.success("openai", LifecycleState.REGISTERED, LifecycleState.VALIDATED, "Validation complete");
        assertTrue(t.success());
        assertEquals(LifecycleState.REGISTERED, t.from());

        var f = StateTransition.failed("openai", LifecycleState.ACTIVE, LifecycleState.FAILED, "Timeout");
        assertFalse(f.success());
        assertEquals("Timeout", f.error());
    }

    @Test
    void shouldCreateLifecycleSnapshot() {
        var snap = new LifecycleSnapshot("snap-1", Instant.now(),
            Map.of("openai", LifecycleState.ACTIVE), 1, 1, 0, 0, 0);
        assertEquals(1, snap.activeProviders());
    }

    @Test
    void shouldHaveHealthStatuses() {
        assertEquals(5, HealthStatus.values().length);
        assertEquals(HealthStatus.HEALTHY, HealthStatus.valueOf("HEALTHY"));
    }

    @Test
    void shouldCreateHealthResponse() {
        var healthy = HealthResponse.healthy("openai");
        assertEquals(HealthStatus.HEALTHY, healthy.status());
        assertTrue(healthy.available());

        var unhealthy = HealthResponse.unhealthy("openai");
        assertEquals(HealthStatus.UNHEALTHY, unhealthy.status());
        assertFalse(unhealthy.available());
    }

    @Test
    void shouldCreateHealthSnapshot() {
        var snap = new HealthSnapshot("snap-1", Instant.now(),
            Map.of(), 0, 0, 0, 0, 0);
        assertEquals(0, snap.totalProviders());
    }

    @Test
    void shouldCreateHealthConfiguration() {
        var config = HealthConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals(Duration.ofSeconds(30), config.checkInterval());
        assertEquals(3, config.failureThreshold());
    }

    @Test
    void shouldHaveReadinessStates() {
        assertEquals(6, ReadinessState.values().length);
        assertEquals(ReadinessState.READY, ReadinessState.valueOf("READY"));
        assertEquals(ReadinessState.NOT_READY, ReadinessState.valueOf("NOT_READY"));
    }

    @Test
    void shouldHaveLivenessStates() {
        assertEquals(6, LivenessState.values().length);
        assertEquals(LivenessState.ALIVE, LivenessState.valueOf("ALIVE"));
        assertEquals(LivenessState.DEAD, LivenessState.valueOf("DEAD"));
        assertEquals(LivenessState.HEARTBEAT_LOST, LivenessState.valueOf("HEARTBEAT_LOST"));
    }

    @Test
    void shouldHaveCircuitStates() {
        assertEquals(3, CircuitState.values().length);
        assertEquals(CircuitState.CLOSED, CircuitState.valueOf("CLOSED"));
        assertEquals(CircuitState.OPEN, CircuitState.valueOf("OPEN"));
        assertEquals(CircuitState.HALF_OPEN, CircuitState.valueOf("HALF_OPEN"));
    }

    @Test
    void shouldCreateCircuitBreakerConfig() {
        var config = CircuitBreakerConfig.defaults();
        assertEquals(5, config.failureThreshold());
        assertEquals(3, config.successThreshold());
        assertTrue(config.automaticReset());
    }

    @Test
    void shouldCreateAvailabilitySnapshot() {
        var snap = new AvailabilitySnapshot("snap-1", Instant.now(),
            Map.of("openai", true), 1, 1, 0, 100.0);
        assertEquals(1, snap.availableCount());
        assertEquals(100.0, snap.overallAvailability());
    }

    @Test
    void shouldCreateRecoveryContext() {
        var ctx = new RecoveryContext("openai", "Timeout",
            null, 0, 3, Instant.now(), Map.of());
        assertTrue(ctx.isRetryAvailable());

        var exhausted = new RecoveryContext("openai", "Timeout",
            null, 3, 3, Instant.now(), Map.of());
        assertFalse(exhausted.isRetryAvailable());
    }

    @Test
    void shouldCreateMaintenanceWindow() {
        var window = new MaintenanceWindow("w-1", "openai", "Upgrade",
            MaintenanceState.SCHEDULED, Instant.now(), Instant.now().plusSeconds(3600), null, "admin");
        assertEquals(MaintenanceState.SCHEDULED, window.state());
        assertEquals("Upgrade", window.reason());
    }

    @Test
    void shouldHaveMaintenanceStates() {
        assertEquals(5, MaintenanceState.values().length);
        assertEquals(MaintenanceState.ACTIVE, MaintenanceState.valueOf("ACTIVE"));
        assertEquals(MaintenanceState.COMPLETED, MaintenanceState.valueOf("COMPLETED"));
    }

    @Test
    void shouldCreateHeartbeatConfiguration() {
        var config = HeartbeatConfiguration.defaults();
        assertTrue(config.enabled());
        assertEquals(Duration.ofSeconds(10), config.interval());
        assertEquals(Duration.ofSeconds(30), config.expiryThreshold());
    }

    @Test
    void shouldCreateHeartbeatTimeout() {
        var timeout = new HeartbeatTimeout("openai",
            Duration.ofSeconds(5), Duration.ofSeconds(30), 3);
        assertTrue(timeout.isExpired(31000));
        assertFalse(timeout.isExpired(29000));
    }

    @Test
    void shouldCreateMetricsModel() {
        var model = new MetricsModel("openai", 45.0, 100.0, 200.0,
            0.01, 0.99, 99.9, 99.95, 30.0,
            2, 2, 0.95, 5.0, 10.0, 50.0, Instant.now());
        assertEquals("openai", model.providerId());
        assertEquals(45.0, model.averageLatency());
        assertEquals(0.95, model.healthScore());
    }

    @Test
    void shouldCreateAuditEvent() {
        var event = new AuditEvent("evt-1", "openai", AuditEventType.LIFECYCLE,
            "system", "Provider activated", "{}", Instant.now());
        assertEquals(AuditEventType.LIFECYCLE, event.eventType());
        assertEquals("system", event.source());
    }

    @Test
    void shouldHaveAuditEventTypes() {
        assertEquals(13, AuditEventType.values().length);
        assertEquals(AuditEventType.HEALTH, AuditEventType.valueOf("HEALTH"));
        assertEquals(AuditEventType.FAILOVER, AuditEventType.valueOf("FAILOVER"));
    }

    @Test
    void shouldGetAllowedTransitions() {
        var activeTransitions = LifecycleTransition.getAllowedTransitions(LifecycleState.ACTIVE);
        assertTrue(activeTransitions.contains(LifecycleState.DEGRADED));
        assertTrue(activeTransitions.contains(LifecycleState.MAINTENANCE));
        assertTrue(activeTransitions.contains(LifecycleState.BUSY));

        var removedTransitions = LifecycleTransition.getAllowedTransitions(LifecycleState.REMOVED);
        assertTrue(removedTransitions.isEmpty());
    }
}
