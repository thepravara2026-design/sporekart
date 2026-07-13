package com.sporekart.ai.automation.infrastructure.monitoring;

import static org.mockito.Mockito.*;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AutomationMonitoringServiceTest {

    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private Counter counter;
    @Mock
    private Timer timer;

    private AutomationMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(meterRegistry.counter(anyString(), anyString(), anyString())).thenReturn(counter);
        when(meterRegistry.timer(anyString())).thenReturn(timer);
        when(meterRegistry.timer(anyString(), anyArray())).thenReturn(timer);

        monitoringService = new AutomationMonitoringService(meterRegistry);
    }

    @Test
    void recordWorkflowShouldIncrementCounter() {
        monitoringService.recordWorkflow();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordJobShouldIncrementCounter() {
        monitoringService.recordJob();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordRetryShouldIncrementCounter() {
        monitoringService.recordRetry();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordEscalationShouldIncrementCounter() {
        monitoringService.recordEscalation();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordJobSuccessShouldIncrementCounter() {
        monitoringService.recordJobSuccess();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordJobFailedShouldIncrementCounter() {
        monitoringService.recordJobFailed();
        verify(counter, times(2)).increment();
    }

    @Test
    void recordWorkflowDurationShouldRecordTimer() {
        monitoringService.recordWorkflowDuration(500);
        verify(timer).record(500L, TimeUnit.MILLISECONDS);
    }

    @Test
    void recordJobExecutionTimeShouldRecordTimer() {
        monitoringService.recordJobExecutionTime(300);
        verify(timer).record(300L, TimeUnit.MILLISECONDS);
    }

    @Test
    void setActiveJobsShouldUpdateGauge() {
        monitoringService.setActiveJobs(5);
    }

    @Test
    void setActiveWorkflowsShouldUpdateGauge() {
        monitoringService.setActiveWorkflows(3);
    }

    @Test
    void constructorShouldRegisterGauges() {
        verify(meterRegistry, times(6)).counter(anyString());
        verify(meterRegistry, times(2)).gauge(anyString(), any(), any());
    }
}
