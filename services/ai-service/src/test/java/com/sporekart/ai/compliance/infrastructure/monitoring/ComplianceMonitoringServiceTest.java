package com.sporekart.ai.compliance.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplianceMonitoringServiceTest {

    private ComplianceMonitoringService monitoringService;
    private MeterRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        monitoringService = new ComplianceMonitoringService(registry);
        monitoringService.init();
    }

    @Test
    void testIncrementValidationTotal() {
        monitoringService.recordValidation(true);
        monitoringService.recordValidation(true);
        assertEquals(2.0, monitoringService.getValidationTotal(), 0.001);
        assertEquals(2.0, monitoringService.getPassedCount(), 0.001);
        assertEquals(0.0, monitoringService.getFailedCount(), 0.001);
    }

    @Test
    void testRecordValidationFailed() {
        monitoringService.recordValidation(false);
        assertEquals(1.0, monitoringService.getValidationTotal(), 0.001);
        assertEquals(0.0, monitoringService.getPassedCount(), 0.001);
        assertEquals(1.0, monitoringService.getFailedCount(), 0.001);
    }

    @Test
    void testRecordViolation() {
        monitoringService.recordViolation();
        monitoringService.recordViolation();
        monitoringService.recordViolation();
        assertEquals(3.0, monitoringService.getViolationsCount(), 0.001);
    }

    @Test
    void testRecordAssessmentLatency() {
        monitoringService.recordAssessmentLatency(100);
        monitoringService.recordAssessmentLatency(200);
        assertEquals(2, registry.get("compliance.assessment.latency").timer().count());
    }

    @Test
    void testSetPendingAssessments() {
        monitoringService.setPendingAssessments(5);
        assertEquals(5, monitoringService.getPendingAssessments());
    }

    @Test
    void testIncrementPendingAssessments() {
        monitoringService.incrementPendingAssessments();
        monitoringService.incrementPendingAssessments();
        assertEquals(2, monitoringService.getPendingAssessments());
    }

    @Test
    void testDecrementPendingAssessments() {
        monitoringService.setPendingAssessments(5);
        monitoringService.decrementPendingAssessments();
        assertEquals(4, monitoringService.getPendingAssessments());
    }

    @Test
    void testRecordValidationMixed() {
        monitoringService.recordValidation(true);
        monitoringService.recordValidation(false);
        monitoringService.recordValidation(true);

        assertEquals(3.0, monitoringService.getValidationTotal(), 0.001);
        assertEquals(2.0, monitoringService.getPassedCount(), 0.001);
        assertEquals(1.0, monitoringService.getFailedCount(), 0.001);
    }
}
