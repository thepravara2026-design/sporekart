package com.sporekart.ai.assistant.infrastructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class AssistantMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(AssistantMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public AssistantMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void recordAssistantUsage(UUID assistantId) {
        meterRegistry.counter("assistant.usage",
                "assistantId", assistantId.toString()).increment();
    }

    public void recordIntentResolution(String intent, boolean success) {
        var timer = timers.computeIfAbsent("assistant.intent.resolution",
                k -> meterRegistry.timer("assistant.intent.resolution",
                        "intent", intent));
        timer.record(() -> {
            if (success) {
                meterRegistry.counter("assistant.intent.resolution.success",
                        "intent", intent).increment();
            } else {
                meterRegistry.counter("assistant.intent.resolution.failure",
                        "intent", intent).increment();
            }
        });
    }

    public void recordTaskExecution(String taskType, long latencyMs, boolean success) {
        var timer = Timer.builder("assistant.task.execution")
                .tag("taskType", taskType)
                .tag("success", String.valueOf(success))
                .register(meterRegistry);
        timer.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordResponseLatency(long latencyMs) {
        var timer = timers.computeIfAbsent("assistant.response.latency",
                k -> meterRegistry.timer("assistant.response.latency"));
        timer.record(latencyMs, TimeUnit.MILLISECONDS);
    }

    public void recordConversationDuration(long durationMs) {
        var timer = timers.computeIfAbsent("assistant.conversation.duration",
                k -> meterRegistry.timer("assistant.conversation.duration"));
        timer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void recordFeedback(int rating) {
        meterRegistry.gauge("assistant.feedback.rating", rating);
        meterRegistry.counter("assistant.feedback.received").increment();
    }

    public void recordWorkflowInvocation(String workflowType) {
        meterRegistry.counter("assistant.workflow.invocations",
                "workflowType", workflowType).increment();
    }
}
