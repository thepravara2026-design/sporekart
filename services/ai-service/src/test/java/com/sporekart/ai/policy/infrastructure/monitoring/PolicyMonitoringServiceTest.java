package com.sporekart.ai.policy.infrastructure.monitoring;
import com.sporekart.ai.policy.infrastructure.monitoring.PolicyMonitoringService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyMonitoringServiceTest {
    private PolicyMonitoringService monitoringService;
    private MeterRegistry registry;

    @BeforeEach void setUp() { registry = new SimpleMeterRegistry(); monitoringService = new PolicyMonitoringService(registry); }

    @Test void testRecordEvaluation() { monitoringService.recordEvaluation(100); assertEquals(1, registry.counter("policy.evaluations.total").count()); }
    @Test void testRecordViolation() { monitoringService.recordViolation(); assertEquals(1, registry.counter("policy.violations.total").count()); }
    @Test void testRecordDecision_Allow() { monitoringService.recordDecision("ALLOW"); assertEquals(1, registry.counter("policy.decisions.allowed").count()); }
    @Test void testRecordDecision_Deny() { monitoringService.recordDecision("DENY"); assertEquals(1, registry.counter("policy.decisions.denied").count()); }
    @Test void testRecordActivation() { monitoringService.recordActivation(); assertEquals(1, registry.counter("policy.activations.total").count()); }
    @Test void testRecordDeactivation() { monitoringService.recordDeactivation(); assertEquals(1, registry.counter("policy.deactivations.total").count()); }
    @Test void testRecordCacheHit() { monitoringService.recordCacheHit(); assertNotNull(registry.get("policy.cache.hits").gauge()); }
    @Test void testSetRegistrySize() { monitoringService.setRegistrySize(10); assertNotNull(registry.get("policy.registry.size").gauge()); }
}
