package com.sporekart.ai.conversation.infrastructure.observability;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConversationMetricsService {

    private final AtomicInteger activeConversations = new AtomicInteger(0);
    private final AtomicInteger totalMessages = new AtomicInteger(0);
    private final AtomicLong totalTokenUsage = new AtomicLong(0);
    private final AtomicLong totalMemoryRetrievals = new AtomicLong(0);
    private final AtomicLong totalMemoryRetrievalLatencyMs = new AtomicLong(0);
    private final AtomicLong totalSummarizations = new AtomicLong(0);
    private final AtomicLong totalSummarizationLatencyMs = new AtomicLong(0);
    private final AtomicInteger activeSessions = new AtomicInteger(0);
    private final AtomicInteger totalContextCompressions = new AtomicInteger(0);

    public void incrementActiveConversations() {
        activeConversations.incrementAndGet();
    }

    public void decrementActiveConversations() {
        activeConversations.decrementAndGet();
    }

    public void recordMessage() {
        totalMessages.incrementAndGet();
    }

    public void recordTokens(int tokens) {
        totalTokenUsage.addAndGet(tokens);
    }

    public void recordMemoryRetrieval(long latencyMs) {
        totalMemoryRetrievals.incrementAndGet();
        totalMemoryRetrievalLatencyMs.addAndGet(latencyMs);
    }

    public void recordSummarization(long latencyMs) {
        totalSummarizations.incrementAndGet();
        totalSummarizationLatencyMs.addAndGet(latencyMs);
    }

    public void recordContextCompression() {
        totalContextCompressions.incrementAndGet();
    }

    public void sessionStarted() {
        activeSessions.incrementAndGet();
    }

    public void sessionEnded() {
        activeSessions.decrementAndGet();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("activeConversations", activeConversations.get());
        metrics.put("totalMessages", totalMessages.get());
        metrics.put("totalTokenUsage", totalTokenUsage.get());

        long retrievals = totalMemoryRetrievals.get();
        double avgRetrievalLatency = retrievals > 0
            ? (double) totalMemoryRetrievalLatencyMs.get() / retrievals
            : 0.0;
        metrics.put("averageMemoryRetrievalLatency", avgRetrievalLatency);
        metrics.put("totalMemoryRetrievals", (int) retrievals);
 
         long summarizations = totalSummarizations.get();
        double avgSummarizationLatency = summarizations > 0
            ? (double) totalSummarizationLatencyMs.get() / summarizations
            : 0.0;
        metrics.put("totalSummarizations", (int) summarizations);
        metrics.put("averageSummarizationLatency", avgSummarizationLatency);

        metrics.put("activeSessions", activeSessions.get());
        metrics.put("totalContextCompressions", totalContextCompressions.get());
        return metrics;
    }

    public void reset() {
        activeConversations.set(0);
        totalMessages.set(0);
        totalTokenUsage.set(0);
        totalMemoryRetrievals.set(0);
        totalMemoryRetrievalLatencyMs.set(0);
        totalSummarizations.set(0);
        totalSummarizationLatencyMs.set(0);
        activeSessions.set(0);
        totalContextCompressions.set(0);
    }
}
