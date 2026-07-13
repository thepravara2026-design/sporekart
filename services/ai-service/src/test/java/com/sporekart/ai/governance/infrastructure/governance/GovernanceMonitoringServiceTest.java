package com.sporekart.ai.governance.infrastructure.governance;

import com.sporekart.ai.governance.infrastructure.monitoring.GovernanceMonitoringService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GovernanceMonitoringServiceTest {

    private GovernanceMonitoringService monitoringService;
    private MeterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        monitoringService = new GovernanceMonitoringService(registry);
    }

    @Test
    void testRecordRequest() {
        monitoringService.recordRequest();
        assertEquals(1, registry.counter("governance.requests.total").count());
    }

    @Test
    void testRecordValidation() {
        monitoringService.recordValidation(100);
        assertEquals(1, registry.counter("governance.validations.total").count());
    }

    @Test
    void testRecordAudit() {
        monitoringService.recordAudit();
        assertEquals(1, registry.counter("governance.audits.total").count());
    }

    @Test
    void testRecordDecision_Allow() {
        monitoringService.recordDecision("ALLOW");
        assertEquals(1, registry.counter("governance.decisions.allowed").count());
    }

    @Test
    void testRecordDecision_Deny() {
        monitoringService.recordDecision("DENY");
        assertEquals(1, registry.counter("governance.decisions.denied").count());
    }

    @Test
    void testRecordConfigReload() {
        monitoringService.recordConfigReload();
        assertEquals(1, registry.counter("governance.config.reloads").count());
    }

    @Test
    void testRecordCacheHit() {
        monitoringService.recordCacheHit();
        monitoringService.recordCacheHit();
        assertNotNull(registry.get("governance.cache.hits").gauge());
    }

    @Test
    void testSetActivePolicies() {
        monitoringService.setActivePolicies(10);
        assertNotNull(registry.get("governance.policies.active").gauge());
    }
}
