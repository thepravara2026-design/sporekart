package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.ContextBuilderService;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.util.*;

public class ContextBuilder implements ContextBuilderService {
    private final KnowledgeMetricsService metricsService;

    public ContextBuilder(KnowledgeMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    public RagContext buildContext(String query, List<RetrievalResult> results, int maxTokens) {
        return buildContextWithBudget(query, results, maxTokens, true);
    }

    @Override
    public RagContext buildContextWithBudget(String query, List<RetrievalResult> results,
                                            int maxTokens, boolean deduplicate) {
        var processed = deduplicate ? deduplicate(results) : results;
        var sorted = processed.stream()
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .toList();

        var sb = new StringBuilder();
        sb.append("Context for query: \"").append(query).append("\"\n\n");
        int totalTokens = 0;
        var sources = new ArrayList<String>();

        for (var result : sorted) {
            var chunk = result.chunk();
            var content = chunk.content();
            int chunkTokens = estimateTokens(content) + 50;
            if (totalTokens + chunkTokens > maxTokens) break;
            sb.append("--- Source: ").append(result.citation().source()).append(" ---\n");
            sb.append(content).append("\n\n");
            totalTokens += chunkTokens;
            sources.add(result.citation().source());
        }

        var assembled = sb.toString();
        metricsService.recordContextBuilt();
        return new RagContext(query, sorted, assembled, totalTokens, maxTokens, sources);
    }

    private List<RetrievalResult> deduplicate(List<RetrievalResult> results) {
        var seen = new HashSet<KnowledgeChunkId>();
        return results.stream()
                .filter(r -> seen.add(r.chunk().id()))
                .toList();
    }

    private int estimateTokens(String text) {
        return text.length() / 4 + 3;
    }
}
