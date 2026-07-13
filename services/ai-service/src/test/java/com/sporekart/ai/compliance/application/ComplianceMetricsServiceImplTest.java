package com.sporekart.ai.compliance.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceMetricsServiceImplTest {

    private ComplianceMetricsServiceImpl metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new ComplianceMetricsServiceImpl();
    }

    @Test
    void testRecordValidationPassed() {
        metricsService.recordValidation(true);
        assertEquals(1, metricsService.getTotalValidations());
        assertEquals(1, metricsService.getPassCount());
        assertEquals(0, metricsService.getFailureCount());
    }

    @Test
    void testRecordValidationFailed() {
        metricsService.recordValidation(false);
        assertEquals(1, metricsService.getTotalValidations());
        assertEquals(0, metricsService.getPassCount());
        assertEquals(1, metricsService.getFailureCount());
    }

    @Test
    void testRecordViolation() {
        metricsService.recordViolation();
        metricsService.recordViolation();
        assertEquals(2, metricsService.getViolationCount());
    }

    @Test
    void testGetPassRate() {
        metricsService.recordValidation(true);
        metricsService.recordValidation(true);
        metricsService.recordValidation(false);
        assertEquals(2.0 / 3.0, metricsService.getPassRate(), 0.001);
    }

    @Test
    void testGetPassRateWhenNoValidations() {
        assertEquals(0.0, metricsService.getPassRate(), 0.001);
    }

    @Test
    void testRecordAssessmentLatency() {
        metricsService.recordAssessmentLatency(100);
        metricsService.recordAssessmentLatency(200);
        assertEquals(150.0, metricsService.getAverageAssessmentLatencyMs(), 0.001);
    }

    @Test
    void testGetAverageAssessmentLatencyWhenNoAssessments() {
        assertEquals(0.0, metricsService.getAverageAssessmentLatencyMs(), 0.001);
    }

    @Test
    void testGetStatistics() {
        metricsService.recordValidation(true);
        metricsService.recordValidation(false);
        metricsService.recordViolation();
        metricsService.recordAssessmentLatency(100);

        Map<String, Object> stats = metricsService.getStatistics();

        assertEquals(2L, stats.get("totalValidations"));
        assertEquals(1L, stats.get("passCount"));
        assertEquals(1L, stats.get("failureCount"));
        assertEquals(1L, stats.get("violationCount"));
        assertEquals(0.5, (double) stats.get("passRate"), 0.001);
        assertEquals(1L, stats.get("assessmentCount"));
        assertEquals(100.0, (double) stats.get("averageAssessmentLatencyMs"), 0.001);
    }
}
