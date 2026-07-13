package com.sporekart.ai.assistant.infrastructure.monitoring;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AssistantMonitoringServiceTest {

    private SimpleMeterRegistry meterRegistry;
    private AssistantMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new AssistantMonitoringService(meterRegistry);
    }

    @Test
    void shouldRecordAssistantUsage() {
        var assistantId = UUID.randomUUID();
        monitoringService.recordAssistantUsage(assistantId);
        assertEquals(1.0, meterRegistry.counter("assistant.usage",
                "assistantId", assistantId.toString()).count());
    }

    @Test
    void shouldRecordIntentResolutionSuccess() {
        monitoringService.recordIntentResolution("product_search", true);
        assertEquals(1.0, meterRegistry.counter("assistant.intent.resolution.success",
                "intent", "product_search").count());
    }

    @Test
    void shouldRecordIntentResolutionFailure() {
        monitoringService.recordIntentResolution("unknown", false);
        assertEquals(1.0, meterRegistry.counter("assistant.intent.resolution.failure",
                "intent", "unknown").count());
    }

    @Test
    void shouldRecordTaskExecution() {
        monitoringService.recordTaskExecution("QueryProducts", 500L, true);
        var timer = meterRegistry.find("assistant.task.execution")
                .tag("taskType", "QueryProducts")
                .tag("success", "true")
                .timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void shouldRecordTaskExecutionFailure() {
        monitoringService.recordTaskExecution("BadTask", 200L, false);
        var timer = meterRegistry.find("assistant.task.execution")
                .tag("taskType", "BadTask")
                .tag("success", "false")
                .timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void shouldRecordResponseLatency() {
        monitoringService.recordResponseLatency(150L);
        var timer = meterRegistry.find("assistant.response.latency").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void shouldRecordConversationDuration() {
        monitoringService.recordConversationDuration(60000L);
        var timer = meterRegistry.find("assistant.conversation.duration").timer();
        assertNotNull(timer);
        assertEquals(1, timer.count());
    }

    @Test
    void shouldRecordFeedback() {
        monitoringService.recordFeedback(4);
        assertEquals(1.0, meterRegistry.counter("assistant.feedback.received").count());
    }

    @Test
    void shouldRecordWorkflowInvocation() {
        monitoringService.recordWorkflowInvocation("order_processing");
        assertEquals(1.0, meterRegistry.counter("assistant.workflow.invocations",
                "workflowType", "order_processing").count());
    }
}
