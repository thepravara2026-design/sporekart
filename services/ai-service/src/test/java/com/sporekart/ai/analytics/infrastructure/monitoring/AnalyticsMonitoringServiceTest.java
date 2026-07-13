package com.sporekart.ai.analytics.infrastructure.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsMonitoringServiceTest {

    @Mock private MeterRegistry meterRegistry;
    @Mock private Counter counter;
    @Mock private Timer timer;

    private AnalyticsMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(meterRegistry.timer(anyString())).thenReturn(timer);
        monitoringService = new AnalyticsMonitoringService(meterRegistry);
    }

    @Test
    void testInitGauges() {
        monitoringService.initGauges();
        verify(meterRegistry, atLeast(2)).gauge(anyString(), any(), any());
    }

    @Test
    void testIncrementDashboardLoads() {
        monitoringService.recordDashboardLoad();
        verify(meterRegistry).counter("analytics.dashboard.loads");
        verify(counter).increment();
    }

    @Test
    void testRecordReportGenerated() {
        monitoringService.recordReportGenerated();
        verify(meterRegistry).counter("analytics.reports.generated");
        verify(counter).increment();
    }

    @Test
    void testRecordExportCompleted() {
        monitoringService.recordExportCompleted();
        verify(meterRegistry).counter("analytics.exports.completed");
        verify(counter).increment();
    }

    @Test
    void testRecordKPICalculation() {
        monitoringService.recordKPICalculation();
        verify(meterRegistry).counter("analytics.kpi.calculations");
        verify(counter).increment();
    }

    @Test
    void testRecordDashboardLoadTime() {
        monitoringService.recordDashboardLoadTime(() -> "result");
        verify(meterRegistry).timer("analytics.dashboard.load.time");
    }

    @Test
    void testRecordReportGenerationTime() {
        monitoringService.recordReportGenerationTime(() -> 42);
        verify(meterRegistry).timer("analytics.report.generation.time");
    }

    @Test
    void testRecordKPICalculationTime() {
        monitoringService.recordKPICalculationTime(() -> true);
        verify(meterRegistry).timer("analytics.kpi.calculation.time");
    }

    @Test
    void testRecordExportTime() {
        monitoringService.recordExportTime(() -> "done");
        verify(meterRegistry).timer("analytics.export.time");
    }

    @Test
    void testUpdateCacheHitRatio() {
        monitoringService.updateCacheHitRatio(0.85);
        verify(meterRegistry).gauge(eq("analytics.cache.hit.ratio"), any(), any());
    }

    @Test
    void testUpdateProcessingTime() {
        monitoringService.updateProcessingTime(150.0);
        verify(meterRegistry).gauge(eq("analytics.processing.time"), any(), any());
    }
}
