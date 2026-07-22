package com.sporekart.ai.knowledge.infrastructure.observability;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class KnowledgeMetricsService {

    private final AtomicLong documentsIngested = new AtomicLong(0);
    private final AtomicLong documentsChunked = new AtomicLong(0);
    private final AtomicLong chunksCreated = new AtomicLong(0);
    private final AtomicLong embeddingsGenerated = new AtomicLong(0);
    private final AtomicLong retrievalsPerformed = new AtomicLong(0);
    private final AtomicLong citationsGenerated = new AtomicLong(0);
    private final AtomicLong contextBuilds = new AtomicLong(0);
    private final AtomicLong totalRetrievalLatencyMs = new AtomicLong(0);
    private final AtomicLong totalEmbeddingLatencyMs = new AtomicLong(0);
    private final AtomicLong totalChunkingLatencyMs = new AtomicLong(0);
    private final AtomicLong totalIndexSize = new AtomicLong(0);

    public void recordDocumentIngested() {
        documentsIngested.incrementAndGet();
    }

    public void recordDocumentChunked() {
        documentsChunked.incrementAndGet();
    }

    public void recordChunkCreated() {
        chunksCreated.incrementAndGet();
    }

    public void recordEmbeddingGenerated() {
        embeddingsGenerated.incrementAndGet();
    }

    public void recordRetrieval(long latencyMs) {
        retrievalsPerformed.incrementAndGet();
        totalRetrievalLatencyMs.addAndGet(latencyMs);
    }

    public void recordCitationGenerated() {
        citationsGenerated.incrementAndGet();
    }

    public void recordContextBuilt() {
        contextBuilds.incrementAndGet();
    }

    public void recordIndexSize(int size) {
        totalIndexSize.addAndGet(size);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("documentsIngested", documentsIngested.get());
        metrics.put("documentsChunked", documentsChunked.get());
        metrics.put("chunksCreated", chunksCreated.get());
        metrics.put("embeddingsGenerated", embeddingsGenerated.get());
        metrics.put("retrievalsPerformed", retrievalsPerformed.get());
        metrics.put("citationsGenerated", citationsGenerated.get());
        metrics.put("contextBuilds", contextBuilds.get());
        metrics.put("totalRetrievalLatencyMs", totalRetrievalLatencyMs.get());
        metrics.put("totalEmbeddingLatencyMs", totalEmbeddingLatencyMs.get());
        metrics.put("totalChunkingLatencyMs", totalChunkingLatencyMs.get());
        metrics.put("totalIndexSize", totalIndexSize.get());

        long retrievals = retrievalsPerformed.get();
        metrics.put("averageRetrievalLatencyMs", retrievals > 0 ? totalRetrievalLatencyMs.get() / retrievals : 0L);

        long embeddings = embeddingsGenerated.get();
        metrics.put("averageEmbeddingLatencyMs", embeddings > 0 ? totalEmbeddingLatencyMs.get() / embeddings : 0L);

        long chunks = chunksCreated.get();
        metrics.put("averageChunkingLatencyMs", chunks > 0 ? totalChunkingLatencyMs.get() / chunks : 0L);

        return metrics;
    }

    public void reset() {
        documentsIngested.set(0);
        documentsChunked.set(0);
        chunksCreated.set(0);
        embeddingsGenerated.set(0);
        retrievalsPerformed.set(0);
        citationsGenerated.set(0);
        contextBuilds.set(0);
        totalRetrievalLatencyMs.set(0);
        totalEmbeddingLatencyMs.set(0);
        totalChunkingLatencyMs.set(0);
        totalIndexSize.set(0);
    }
}
