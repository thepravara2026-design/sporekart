package com.sporekart.ai.admin.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminMonitoringServiceTest {

    private MeterRegistry meterRegistry;
    private AdminMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new AdminMonitoringService(meterRegistry);
    }

    @Test
    void testRecordOperation() {
        monitoringService.recordOperation();
        var counter = meterRegistry.counter("admin.operations.total");
        assertEquals(1.0, counter.count(), 0.001);
    }

    @Test
    void testRecordConfigurationChange() {
        monitoringService.recordConfigurationChange();
        var counter = meterRegistry.counter("admin.config.changes");
        assertEquals(1.0, counter.count(), 0.001);
    }

    @Test
    void testRecordFeatureFlagChange() {
        monitoringService.recordFeatureFlagChange();
        var counter = meterRegistry.counter("admin.feature.flag.changes");
        assertEquals(1.0, counter.count(), 0.001);
    }

    @Test
    void testRecordRollback() {
        monitoringService.recordRollback();
        var counter = meterRegistry.counter("admin.rollbacks");
        assertEquals(1.0, counter.count(), 0.001);
    }

    @Test
    void testRecordConfigurationValidationTime() {
        monitoringService.recordConfigurationValidationTime(150);
        var timer = meterRegistry.timer("admin.config.validation.time");
        assertEquals(1, timer.count());
    }

    @Test
    void testRecordOperationTime() {
        monitoringService.recordOperationTime(200);
        var timer = meterRegistry.timer("admin.operation.time");
        assertEquals(1, timer.count());
    }

    @Test
    void testSetActiveConfigs() {
        monitoringService.setActiveConfigs(42);
        var gauge = meterRegistry.gauge("admin.active.configs");
        assertNotNull(gauge);
    }

    @Test
    void testSetActiveFeatureFlags() {
        monitoringService.setActiveFeatureFlags(10);
        var gauge = meterRegistry.gauge("admin.active.feature.flags");
        assertNotNull(gauge);
    }

    @Test
    void testMultipleOperationsAreRecorded() {
        monitoringService.recordOperation();
        monitoringService.recordOperation();
        monitoringService.recordOperation();
        var counter = meterRegistry.counter("admin.operations.total");
        assertEquals(3.0, counter.count(), 0.001);
    }
}
