package com.sporekart.workspace.infrastructure.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

@Component
public class WorkspaceMetricsService {

    private static final Logger log = LoggerFactory.getLogger(WorkspaceMetricsService.class);

    private final ConcurrentHashMap<String, LongAdder> routingCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, DoubleAdder> routingLatency = new ConcurrentHashMap<>();
    private final LongAdder handoffSuccessCount = new LongAdder();
    private final LongAdder handoffFailureCount = new LongAdder();
    private final LongAdder handoffTotalCount = new LongAdder();
    private final ConcurrentHashMap<String, LongAdder> handoffCounters = new ConcurrentHashMap<>();
    private final LongAdder collaborationCount = new LongAdder();
    private final DoubleAdder collaborationDuration = new DoubleAdder();
    private final LongAdder conversationCount = new LongAdder();
    private final DoubleAdder conversationDuration = new DoubleAdder();
    private final LongAdder knowledgeAccessCount = new LongAdder();
    private final LongAdder knowledgeResultsCount = new LongAdder();
    private final ConcurrentHashMap<String, LongAdder> tokenPromptCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, LongAdder> tokenCompletionCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, DoubleAdder> tokenCostCounters = new ConcurrentHashMap<>();
    private final AtomicLong totalRoutingCount = new AtomicLong(0);
    private final AtomicInteger collaborationCopilotSum = new AtomicInteger(0);

    public void recordRouting(String copilotType, double confidence, long durationMs) {
        routingCounters.computeIfAbsent(copilotType, k -> new LongAdder()).increment();
        routingLatency.computeIfAbsent(copilotType, k -> new DoubleAdder()).add(durationMs);
        totalRoutingCount.incrementAndGet();
    }

    public void recordHandoff(String from, String to, boolean success) {
        String key = from + "->" + to;
        handoffCounters.computeIfAbsent(key, k -> new LongAdder()).increment();
        handoffTotalCount.increment();
        if (success) {
            handoffSuccessCount.increment();
        } else {
            handoffFailureCount.increment();
        }
    }

    public void recordCollaboration(int copilotCount, long durationMs) {
        collaborationCount.increment();
        collaborationDuration.add(durationMs);
        collaborationCopilotSum.addAndGet(copilotCount);
    }

    public void recordConversation(String copilotType, long durationMs) {
        conversationCount.increment();
        conversationDuration.add(durationMs);
    }

    public void recordKnowledgeAccess(String source, int resultsCount) {
        knowledgeAccessCount.increment();
        knowledgeResultsCount.add(resultsCount);
    }

    public void recordTokenUsage(String provider, int promptTokens, int completionTokens, double cost) {
        tokenPromptCounters.computeIfAbsent(provider, k -> new LongAdder()).add(promptTokens);
        tokenCompletionCounters.computeIfAbsent(provider, k -> new LongAdder()).add(completionTokens);
        tokenCostCounters.computeIfAbsent(provider, k -> new DoubleAdder()).add(cost);
    }

    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> summary = new HashMap<>();

        Map<String, Object> routing = new HashMap<>();
        routing.put("total", totalRoutingCount.get());
        Map<String, Object> routingByType = new HashMap<>();
        for (Map.Entry<String, LongAdder> entry : routingCounters.entrySet()) {
            Map<String, Object> typeStats = new HashMap<>();
            typeStats.put("count", entry.getValue().sum());
            DoubleAdder latency = routingLatency.get(entry.getKey());
            typeStats.put("totalLatencyMs", latency != null ? latency.sum() : 0.0);
            routingByType.put(entry.getKey(), typeStats);
        }
        routing.put("byCopilotType", routingByType);
        summary.put("routing", routing);

        Map<String, Object> handoff = new HashMap<>();
        handoff.put("total", handoffTotalCount.sum());
        handoff.put("success", handoffSuccessCount.sum());
        handoff.put("failure", handoffFailureCount.sum());
        Map<String, Object> handoffDetails = new HashMap<>();
        for (Map.Entry<String, LongAdder> entry : handoffCounters.entrySet()) {
            handoffDetails.put(entry.getKey(), entry.getValue().sum());
        }
        handoff.put("details", handoffDetails);
        summary.put("handoff", handoff);

        Map<String, Object> collaboration = new HashMap<>();
        collaboration.put("count", collaborationCount.sum());
        collaboration.put("totalDurationMs", collaborationDuration.sum());
        collaboration.put("avgCopilotCount", collaborationCount.sum() > 0
                ? (double) collaborationCopilotSum.get() / collaborationCount.sum() : 0.0);
        summary.put("collaboration", collaboration);

        Map<String, Object> conversation = new HashMap<>();
        conversation.put("count", conversationCount.sum());
        conversation.put("totalDurationMs", conversationDuration.sum());
        conversation.put("avgDurationMs", conversationCount.sum() > 0
                ? conversationDuration.sum() / conversationCount.sum() : 0.0);
        summary.put("conversation", conversation);

        Map<String, Object> knowledge = new HashMap<>();
        knowledge.put("accessCount", knowledgeAccessCount.sum());
        knowledge.put("totalResults", knowledgeResultsCount.sum());
        knowledge.put("avgResultsPerAccess", knowledgeAccessCount.sum() > 0
                ? (double) knowledgeResultsCount.sum() / knowledgeAccessCount.sum() : 0.0);
        summary.put("knowledge", knowledge);

        Map<String, Object> tokenUsage = new HashMap<>();
        for (String provider : tokenPromptCounters.keySet()) {
            Map<String, Object> providerStats = new HashMap<>();
            providerStats.put("promptTokens", tokenPromptCounters.get(provider).sum());
            providerStats.put("completionTokens", tokenCompletionCounters.get(provider).sum());
            DoubleAdder cost = tokenCostCounters.get(provider);
            providerStats.put("cost", cost != null ? cost.sum() : 0.0);
            tokenUsage.put(provider, providerStats);
        }
        summary.put("tokenUsage", tokenUsage);

        return summary;
    }

    public void resetMetrics() {
        routingCounters.clear();
        routingLatency.clear();
        handoffTotalCount.reset();
        handoffSuccessCount.reset();
        handoffFailureCount.reset();
        handoffCounters.clear();
        collaborationCount.reset();
        collaborationDuration.reset();
        collaborationCopilotSum.set(0);
        conversationCount.reset();
        conversationDuration.reset();
        knowledgeAccessCount.reset();
        knowledgeResultsCount.reset();
        tokenPromptCounters.clear();
        tokenCompletionCounters.clear();
        tokenCostCounters.clear();
        totalRoutingCount.set(0);
        log.info("All metrics have been reset");
    }
}
