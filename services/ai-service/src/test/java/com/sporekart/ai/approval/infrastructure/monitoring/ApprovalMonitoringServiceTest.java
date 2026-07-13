package com.sporekart.ai.approval.infrastructure.monitoring;

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
class ApprovalMonitoringServiceTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @Mock
    private Timer timer;

    private ApprovalMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        when(meterRegistry.counter(anyString())).thenReturn(counter);
        when(meterRegistry.timer(anyString())).thenReturn(timer);
        monitoringService = new ApprovalMonitoringService(meterRegistry);
        monitoringService.init();
    }

    @Test
    void shouldIncrementTotalRequests() {
        monitoringService.incrementTotalRequests();
        verify(counter).increment();
    }

    @Test
    void shouldIncrementApproved() {
        monitoringService.incrementApproved();
        verify(counter).increment();
    }

    @Test
    void shouldIncrementRejected() {
        monitoringService.incrementRejected();
        verify(counter).increment();
    }

    @Test
    void shouldIncrementEscalated() {
        monitoringService.incrementEscalated();
        verify(counter).increment();
    }

    @Test
    void shouldIncrementDelegated() {
        monitoringService.incrementDelegated();
        verify(counter).increment();
    }

    @Test
    void shouldIncrementExpired() {
        monitoringService.incrementExpired();
        verify(counter).increment();
    }

    @Test
    void shouldRecordReviewTime() {
        monitoringService.recordReviewTime(500);
        verify(timer).record(eq(500L), eq(java.util.concurrent.TimeUnit.MILLISECONDS));
    }
}
