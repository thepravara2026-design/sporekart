package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.api.KnowledgeSourceRepository;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.util.*;

public class KnowledgeAnalyticsService {
    private final KnowledgeSourceRepository sourceRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeMetricsService metricsService;

    public KnowledgeAnalyticsService(KnowledgeSourceRepository sourceRepository,
                                     KnowledgeDocumentRepository documentRepository,
                                     KnowledgeChunkRepository chunkRepository,
                                     KnowledgeMetricsService metricsService) {
        this.sourceRepository = sourceRepository;
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.metricsService = metricsService;
    }

    public Map<String, Object> getPlatformMetrics() {
        var metrics = metricsService.getMetrics();
        metrics.put("totalSources", sourceRepository.findAll().size());
        metrics.put("totalDocuments", documentRepository.findAll().size());
        metrics.put("totalChunks", chunkRepository.findAll().size());
        metrics.put("indexedDocuments",
                (int) documentRepository.findAll().stream().filter(d -> d.chunkCount() > 0).count());
        return metrics;
    }

    public Map<String, Object> getWorkspaceMetrics(String workspaceId) {
        var sources = sourceRepository.findByWorkspaceId(workspaceId);
        int totalDocs = 0;
        int totalChunks = 0;
        for (var source : sources) {
            totalDocs += documentRepository.findBySourceId(source.id()).size();
        }
        var docs = documentRepository.findAll();
        for (var doc : docs) {
            totalChunks += chunkRepository.findByDocumentId(doc.id()).size();
        }
        var m = new HashMap<String, Object>();
        m.put("workspaceId", workspaceId);
        m.put("sources", sources.size());
        m.put("documents", totalDocs);
        m.put("chunks", totalChunks);
        return m;
    }
}
