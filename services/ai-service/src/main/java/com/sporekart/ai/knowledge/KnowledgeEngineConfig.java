package com.sporekart.ai.knowledge;

import com.sporekart.ai.knowledge.api.*;
import com.sporekart.ai.knowledge.application.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KnowledgeEngineConfig {

    @Bean public KnowledgeSourceRepository knowledgeSourceRepository() { return new InMemoryKnowledgeSourceRepository(); }
    @Bean public KnowledgeDocumentRepository knowledgeDocumentRepository() { return new InMemoryKnowledgeDocumentRepository(); }
    @Bean public KnowledgeChunkRepository knowledgeChunkRepository() { return new InMemoryKnowledgeChunkRepository(); }
    @Bean public KnowledgeCollectionRepository knowledgeCollectionRepository() { return new InMemoryKnowledgeCollectionRepository(); }

    @Bean public KnowledgeMetricsService knowledgeMetricsService() { return new KnowledgeMetricsService(); }

    @Bean public KnowledgeRegistryService knowledgeRegistryService(
            KnowledgeSourceRepository sourceRepo, KnowledgeCollectionRepository collectionRepo) {
        return new KnowledgeRegistryManager(sourceRepo, collectionRepo);
    }

    @Bean public DocumentIngestionService documentIngestionService(
            KnowledgeDocumentRepository docRepo, KnowledgeChunkRepository chunkRepo,
            KnowledgeSourceRepository sourceRepo, DocumentChunkingEngine chunkingEngine) {
        return new DocumentIngestionManager(docRepo, chunkRepo, sourceRepo, chunkingEngine);
    }

    @Bean public DocumentChunkingEngine documentChunkingEngine() { return new DocumentChunkingEngine(); }

    @Bean public EmbeddingService embeddingService(
            KnowledgeChunkRepository chunkRepo, KnowledgeDocumentRepository docRepo,
            KnowledgeMetricsService metrics) {
        return new EmbeddingPipeline(chunkRepo, docRepo, metrics);
    }

    @Bean public VectorIndexManager vectorIndexManager(
            KnowledgeChunkRepository chunkRepo, EmbeddingPipeline embeddingPipeline,
            KnowledgeMetricsService metrics) {
        return new VectorIndexManager(chunkRepo, embeddingPipeline, metrics);
    }

    @Bean public SemanticRetrievalService semanticRetrievalService(
            VectorIndexManager vectorIndex, EmbeddingPipeline embeddingPipeline,
            KnowledgeChunkRepository chunkRepo, KnowledgeMetricsService metrics) {
        return new SemanticRetrievalEngine(vectorIndex, embeddingPipeline, chunkRepo, metrics);
    }

    @Bean public ContextBuilderService contextBuilderService(KnowledgeMetricsService metrics) {
        return new ContextBuilder(metrics);
    }

    @Bean public CitationGenerator citationGenerator(KnowledgeMetricsService metrics) {
        return new CitationGenerator(metrics);
    }

    @Bean public RagOrchestratorService ragOrchestratorService(
            SemanticRetrievalService retrieval, ContextBuilderService contextBuilder,
            CitationGenerator citationGen, KnowledgeMetricsService metrics) {
        return new RagPipeline(retrieval, contextBuilder, citationGen, metrics);
    }

    @Bean public KnowledgeAnalyticsService knowledgeAnalyticsService(
            KnowledgeSourceRepository sourceRepo, KnowledgeDocumentRepository docRepo,
            KnowledgeChunkRepository chunkRepo, KnowledgeMetricsService metrics) {
        return new KnowledgeAnalyticsService(sourceRepo, docRepo, chunkRepo, metrics);
    }
}
