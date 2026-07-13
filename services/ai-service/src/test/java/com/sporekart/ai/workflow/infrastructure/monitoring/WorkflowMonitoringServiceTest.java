package com.sporekart.ai.workflow.infrastructure.monitoring;

import com.sporekart.ai.workflow.api.WorkflowService;
import com.sporekart.ai.workflow.infrastructure.redis.WorkflowRedisCacheService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkflowMonitoringServiceTest {

    @Mock
    private WorkflowService workflowService;

    @Mock
    private WorkflowRedisCacheService cacheService;

    private SimpleMeterRegistry meterRegistry;
    private WorkflowMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new WorkflowMonitoringService(meterRegistry, workflowService, cacheService);
    }

    @Test
    void shouldRecordExecutionLatency() {
        var result = monitoringService.recordExecutionLatency(() -> "test");
        assertEquals("test", result);
    }

    @Test
    void shouldRecordStepLatency() {
        var result = monitoringService.recordStepLatency(() -> 42);
        assertEquals(42, result);
    }

    @Test
    void shouldRecordExecutionStarted() {
        monitoringService.recordExecutionStarted();
        var count = meterRegistry.counter("workflow.execution.started").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordExecutionCompleted() {
        monitoringService.recordExecutionCompleted();
        var count = meterRegistry.counter("workflow.execution.completed").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordExecutionFailed() {
        monitoringService.recordExecutionFailed();
        var count = meterRegistry.counter("workflow.execution.failed").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordStepCompleted() {
        monitoringService.recordStepCompleted("AI_GATEWAY");
        var count = meterRegistry.counter("workflow.step.completed", "stepType", "AI_GATEWAY").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordCacheHit() {
        monitoringService.recordCacheHit("definitions");
        var count = meterRegistry.counter("workflow.cache.hit", "cache", "definitions").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldRecordCacheMiss() {
        monitoringService.recordCacheMiss("definitions");
        var count = meterRegistry.counter("workflow.cache.miss", "cache", "definitions").count();
        assertEquals(1.0, count);
    }

    @Test
    void shouldReturnZeroCacheHitRatioWhenNoData() {
        var ratio = monitoringService.getCacheHitRatio("definitions");
        assertEquals(0.0, ratio);
    }

    @Test
    void shouldReturnCacheHitRatio() {
        monitoringService.recordCacheHit("definitions");
        monitoringService.recordCacheMiss("definitions");
        monitoringService.recordCacheMiss("definitions");
        var ratio = monitoringService.getCacheHitRatio("definitions");
        assertEquals(1.0 / 3.0, ratio, 0.001);
    }

    @Test
    void shouldReturnUpHealth() {
        when(workflowService.listDefinitions()).thenReturn(List.of());
        var health = monitoringService.checkHealth();
        assertEquals("UP", health.status());
    }

    @Test
    void shouldReturnDownHealthOnError() {
        when(workflowService.listDefinitions()).thenThrow(new RuntimeException("DB down"));
        var health = monitoringService.checkHealth();
        assertEquals("DOWN", health.status());
    }
}
