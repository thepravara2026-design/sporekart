package com.sporekart.ai.risk.infrastructure.monitoring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@ExtendWith(MockitoExtension.class)
class RiskMonitoringServiceTest {

    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter;
    @Mock
    private Timer timer;

    private RiskMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(meterRegistry.timer(anyString())).thenReturn(timer);

        monitoringService = new RiskMonitoringService(meterRegistry);
        monitoringService.init();
    }

    @Test
    void initShouldRegisterCountersAndTimers() {
        verify(meterRegistry).counter("risk.assessments.total");
        verify(meterRegistry).counter("risk.high.count");
        verify(meterRegistry).counter("risk.critical.count");
        verify(meterRegistry).counter("risk.recommendations");
        verify(meterRegistry).timer("risk.assessment.latency");
        verify(meterRegistry).timer("risk.scoring.latency");
        verify(meterRegistry, times(3)).gauge(anyString(), any(), any(Supplier.class));
    }

    @Test
    void recordRequestShouldIncrementCounter() {
        monitoringService.recordRequest();
        verify(counter).increment();
    }

    @Test
    void recordAssessmentShouldRecordTimer() {
        monitoringService.recordAssessment(100L);
        verify(timer).record(100L, TimeUnit.MILLISECONDS);
    }

    @Test
    void recordRecalculationShouldRecordTimer() {
        monitoringService.recordRecalculation(200L);
        verify(timer).record(200L, TimeUnit.MILLISECONDS);
    }

    @Test
    void recordScoringShouldRecordTimer() {
        monitoringService.recordScoring(50L);
        verify(timer).record(50L, TimeUnit.MILLISECONDS);
    }

    @Test
    void recordRiskLevelHighShouldIncrementCounter() {
        monitoringService.recordRiskLevel("HIGH");
        verify(counter).increment();
    }

    @Test
    void recordRiskLevelCriticalShouldIncrementCounter() {
        monitoringService.recordRiskLevel("CRITICAL");
        verify(counter).increment();
    }

    @Test
    void recordRiskLevelLowShouldNotIncrementAny() {
        monitoringService.recordRiskLevel("LOW");
        verify(counter, never()).increment();
    }

    @Test
    void recordRecommendationShouldIncrementCounter() {
        monitoringService.recordRecommendation();
        verify(counter).increment();
    }

    @Test
    void updateAverageScoreShouldNotThrow() {
        monitoringService.updateAverageScore(65.0);
    }

    @Test
    void updateTrustAverageShouldNotThrow() {
        monitoringService.updateTrustAverage(80.0);
    }

    @Test
    void updateConfidenceAverageShouldNotThrow() {
        monitoringService.updateConfidenceAverage(75.0);
    }

    @Test
    void checkHealthShouldReturnStatusMap() {
        when(counter.count()).thenReturn(5.0);

        var health = monitoringService.checkHealth();

        assertEquals("UP", health.get("status"));
        assertEquals("risk", health.get("service"));
        assertTrue(health.containsKey("timestamp"));
        assertTrue(health.containsKey("details"));
    }
}
