package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.ContextBuilderService;
import com.sporekart.ai.knowledge.api.RagOrchestratorService;
import com.sporekart.ai.knowledge.api.SemanticRetrievalService;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.util.*;

public class RagPipeline implements RagOrchestratorService {
    private final SemanticRetrievalService retrievalService;
    private final ContextBuilderService contextBuilder;
    private final CitationGenerator citationGenerator;
    private final KnowledgeMetricsService metricsService;

    public RagPipeline(SemanticRetrievalService retrievalService,
                       ContextBuilderService contextBuilder,
                       CitationGenerator citationGenerator,
                       KnowledgeMetricsService metricsService) {
        this.retrievalService = retrievalService;
        this.contextBuilder = contextBuilder;
        this.citationGenerator = citationGenerator;
        this.metricsService = metricsService;
    }

    @Override
    public RagContext executeQuery(String query, String workspaceId, int topK,
                                   RetrievalStrategy strategy, int maxContextTokens,
                                   EmbeddingProvider embeddingProvider,
                                   Map<String, String> filters) {
        var results = retrievalService.search(query, topK, strategy, workspaceId, filters);
        var context = contextBuilder.buildContext(query, results, maxContextTokens);
        citationGenerator.generateCitations(query, results);
        metricsService.recordRetrieval(0);
        return context;
    }

    @Override
    public List<Citation> getCitations(String query, RetrievalResult result) {
        return citationGenerator.generateCitations(query, List.of(result));
    }
}
