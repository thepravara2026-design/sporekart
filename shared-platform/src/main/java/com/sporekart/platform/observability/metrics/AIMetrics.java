package com.sporekart.platform.observability.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.DistributionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AIMetrics {

    private static final Logger log = LoggerFactory.getLogger(AIMetrics.class);
    private final MeterRegistry registry;

    private final Counter aiRequests;
    private final Counter aiCompletions;
    private final Counter aiFailures;
    private final Counter providerFailures;
    private final Counter fallbackCount;
    private final Counter promptInjections;
    private final Timer promptBuildTime;
    private final Timer knowledgeRetrievalTime;
    private final Timer memoryLookupTime;
    private final Timer vectorSearchTime;
    private final Timer embeddingGenerationTime;
    private final Timer completionTime;
    private final Timer providerLatency;
    private final DistributionSummary tokenCount;
    private final DistributionSummary promptSize;
    private final DistributionSummary conversationLength;
    private final DistributionSummary aiCost;

    public AIMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.aiRequests = Counter.builder("sporekart.ai.requests.total")
            .description("Total AI requests").register(registry);
        this.aiCompletions = Counter.builder("sporekart.ai.completions.total")
            .description("Total AI completions").register(registry);
        this.aiFailures = Counter.builder("sporekart.ai.failures.total")
            .description("Total AI failures").register(registry);
        this.providerFailures = Counter.builder("sporekart.ai.provider.failures")
            .description("Total provider failures").register(registry);
        this.fallbackCount = Counter.builder("sporekart.ai.fallback.count")
            .description("Total fallback activations").register(registry);
        this.promptInjections = Counter.builder("sporekart.ai.security.prompt_injections")
            .description("Prompt injection attempts detected").register(registry);

        this.promptBuildTime = Timer.builder("sporekart.ai.prompt.build.time")
            .description("Prompt building time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.knowledgeRetrievalTime = Timer.builder("sporekart.ai.knowledge.retrieval.time")
            .description("Knowledge retrieval time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.memoryLookupTime = Timer.builder("sporekart.ai.memory.lookup.time")
            .description("Memory lookup time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.vectorSearchTime = Timer.builder("sporekart.ai.vector.search.time")
            .description("Vector search time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.embeddingGenerationTime = Timer.builder("sporekart.ai.embedding.generation.time")
            .description("Embedding generation time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.completionTime = Timer.builder("sporekart.ai.completion.time")
            .description("AI completion time")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.providerLatency = Timer.builder("sporekart.ai.provider.latency")
            .description("Provider latency")
            .publishPercentiles(0.5, 0.95, 0.99)
            .tag("provider", "unknown")
            .register(registry);

        this.tokenCount = DistributionSummary.builder("sporekart.ai.tokens.count")
            .description("Token count per request")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.promptSize = DistributionSummary.builder("sporekart.ai.prompt.size")
            .description("Prompt size in characters")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.conversationLength = DistributionSummary.builder("sporekart.ai.conversation.length")
            .description("Conversation turns count")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);
        this.aiCost = DistributionSummary.builder("sporekart.ai.cost")
            .description("AI cost per request in cents")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);

        log.info("AI metrics initialized: 6 counters, 7 timers, 4 summaries");
    }

    public void recordAiRequest() { aiRequests.increment(); }
    public void recordAiCompletion() { aiCompletions.increment(); }
    public void recordAiFailure() { aiFailures.increment(); }
    public void recordProviderFailure(String provider) {
        Counter.builder("sporekart.ai.provider.failures")
            .tag("provider", provider).register(registry).increment();
        providerFailures.increment();
    }
    public void recordFallback(String from, String to) {
        Counter.builder("sporekart.ai.fallback.count")
            .tag("from", from).tag("to", to).register(registry).increment();
        fallbackCount.increment();
    }
    public void recordPromptInjection() { promptInjections.increment(); }

    public void recordPromptBuildTime(long millis) {
        promptBuildTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordKnowledgeRetrievalTime(long millis) {
        knowledgeRetrievalTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordMemoryLookupTime(long millis) {
        memoryLookupTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordVectorSearchTime(long millis) {
        vectorSearchTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordEmbeddingGenerationTime(long millis) {
        embeddingGenerationTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordCompletionTime(long millis) {
        completionTime.record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordProviderLatency(String provider, long millis) {
        Timer.builder("sporekart.ai.provider.latency")
            .tag("provider", provider)
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry)
            .record(millis, TimeUnit.MILLISECONDS);
    }

    public void recordTokenCount(int count) { tokenCount.record(count); }
    public void recordPromptSize(int chars) { promptSize.record(chars); }
    public void recordConversationLength(int turns) { conversationLength.record(turns); }
    public void recordAiCost(double cents) { aiCost.record(cents); }
}
