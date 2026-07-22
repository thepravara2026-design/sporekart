package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.time.Instant;
import java.util.*;

public class CitationGenerator {
    private final KnowledgeMetricsService metricsService;

    public CitationGenerator(KnowledgeMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    public List<Citation> generateCitations(String query, List<RetrievalResult> results) {
        var citations = new ArrayList<Citation>();
        for (var result : results) {
            var chunk = result.chunk();
            var citation = new Citation(
                    chunk.documentId(),
                    chunk.id(),
                    result.retrievalStrategy(),
                    null,
                    chunk.heading(),
                    "1.0",
                    "",
                    result.score(),
                    result.score(),
                    chunk.content().substring(0, Math.min(200, chunk.content().length())),
                    Instant.now()
            );
            citations.add(citation);
        }
        metricsService.recordCitationGenerated();
        return citations;
    }

    public Map<String, Object> formatCitations(List<Citation> citations) {
        var formatted = new HashMap<String, Object>();
        var list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < citations.size(); i++) {
            var c = citations.get(i);
            var entry = new HashMap<String, Object>();
            entry.put("index", i + 1);
            entry.put("documentId", c.documentId().toString());
            entry.put("chunkId", c.chunkId().toString());
            entry.put("source", c.source());
            entry.put("section", c.section());
            entry.put("page", c.page());
            entry.put("confidence", c.confidence());
            entry.put("excerpt", c.excerpt());
            list.add(entry);
        }
        formatted.put("citations", list);
        formatted.put("totalCitations", citations.size());
        formatted.put("coverageScore", calculateCoverage(citations));
        return formatted;
    }

    private double calculateCoverage(List<Citation> citations) {
        if (citations.isEmpty()) return 0.0;
        return citations.stream().mapToDouble(Citation::confidence).average().orElse(0.0);
    }
}
