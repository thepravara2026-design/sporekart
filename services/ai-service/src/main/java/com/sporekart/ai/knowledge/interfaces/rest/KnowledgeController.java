package com.sporekart.ai.knowledge.interfaces.rest;

import com.sporekart.ai.knowledge.api.*;
import com.sporekart.ai.knowledge.application.CitationGenerator;
import com.sporekart.ai.knowledge.application.KnowledgeAnalyticsService;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import com.sporekart.ai.knowledge.interfaces.rest.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ai/knowledge")
@CrossOrigin(origins = "*")
public class KnowledgeController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeController.class);

    private final KnowledgeRegistryService knowledgeRegistryService;
    private final DocumentIngestionService documentIngestionService;
    private final EmbeddingService embeddingService;
    private final SemanticRetrievalService semanticRetrievalService;
    private final ContextBuilderService contextBuilderService;
    private final RagOrchestratorService ragOrchestratorService;
    private final CitationGenerator citationGenerator;
    private final KnowledgeAnalyticsService knowledgeAnalyticsService;
    private final KnowledgeMetricsService knowledgeMetricsService;

    public KnowledgeController(KnowledgeRegistryService knowledgeRegistryService,
                               DocumentIngestionService documentIngestionService,
                               EmbeddingService embeddingService,
                               SemanticRetrievalService semanticRetrievalService,
                               ContextBuilderService contextBuilderService,
                               RagOrchestratorService ragOrchestratorService,
                               CitationGenerator citationGenerator,
                               KnowledgeAnalyticsService knowledgeAnalyticsService,
                               KnowledgeMetricsService knowledgeMetricsService) {
        this.knowledgeRegistryService = knowledgeRegistryService;
        this.documentIngestionService = documentIngestionService;
        this.embeddingService = embeddingService;
        this.semanticRetrievalService = semanticRetrievalService;
        this.contextBuilderService = contextBuilderService;
        this.ragOrchestratorService = ragOrchestratorService;
        this.citationGenerator = citationGenerator;
        this.knowledgeAnalyticsService = knowledgeAnalyticsService;
        this.knowledgeMetricsService = knowledgeMetricsService;
    }

    @PostMapping("/sources")
    public ResponseEntity<KnowledgeSourceResponse> registerSource(@RequestBody RegisterSourceRequest request) {
        log.info("Registering knowledge source: {}", request.getName());
        DocumentType type = DocumentType.valueOf(request.getSourceType().toUpperCase());
        KnowledgeSource source = knowledgeRegistryService.registerSource(
                request.getName(), request.getDescription(), request.getWorkspaceId(),
                request.getOwner(), type, request.getSourceUrl(), request.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(toSourceResponse(source));
    }

    @GetMapping("/sources")
    public ResponseEntity<List<KnowledgeSourceResponse>> listSources(
            @RequestParam(required = false) String workspaceId,
            @RequestParam(required = false) String status) {
        DocumentStatus docStatus = status != null ? DocumentStatus.valueOf(status.toUpperCase()) : null;
        List<KnowledgeSource> sources = knowledgeRegistryService.listSources(workspaceId, docStatus);
        List<KnowledgeSourceResponse> responses = sources.stream()
                .map(this::toSourceResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/sources/{id}")
    public ResponseEntity<KnowledgeSourceResponse> getSource(@PathVariable String id) {
        KnowledgeSource source = knowledgeRegistryService.getSource(KnowledgeSourceId.fromString(id));
        return ResponseEntity.ok(toSourceResponse(source));
    }

    @DeleteMapping("/sources/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable String id) {
        knowledgeRegistryService.deleteSource(KnowledgeSourceId.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sources/{id}/archive")
    public ResponseEntity<KnowledgeSourceResponse> archiveSource(@PathVariable String id) {
        knowledgeRegistryService.archiveSource(KnowledgeSourceId.fromString(id));
        KnowledgeSource source = knowledgeRegistryService.getSource(KnowledgeSourceId.fromString(id));
        return ResponseEntity.ok(toSourceResponse(source));
    }

    @PutMapping("/sources/{id}/publish")
    public ResponseEntity<KnowledgeSourceResponse> publishSource(@PathVariable String id) {
        knowledgeRegistryService.publishSource(KnowledgeSourceId.fromString(id));
        KnowledgeSource source = knowledgeRegistryService.getSource(KnowledgeSourceId.fromString(id));
        return ResponseEntity.ok(toSourceResponse(source));
    }

    @PostMapping("/documents")
    public ResponseEntity<KnowledgeDocumentResponse> ingestDocument(@RequestBody IngestDocumentRequest request) {
        log.info("Ingesting document: {}", request.getTitle());
        DocumentType type = request.getDocumentType() != null
                ? DocumentType.valueOf(request.getDocumentType().toUpperCase())
                : DocumentType.UNKNOWN;
        KnowledgeDocument document = documentIngestionService.ingestDocument(
                KnowledgeSourceId.fromString(request.getSourceId()), request.getTitle(),
                type, request.getContent(), request.getAuthor(), request.getLanguage());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDocumentResponse(document));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<KnowledgeDocumentResponse>> listDocuments(
            @RequestParam(required = false) String sourceId,
            @RequestParam(required = false) String status) {
        KnowledgeSourceId srcId = sourceId != null ? KnowledgeSourceId.fromString(sourceId) : null;
        DocumentStatus docStatus = status != null ? DocumentStatus.valueOf(status.toUpperCase()) : null;
        List<KnowledgeDocument> documents = documentIngestionService.listDocuments(srcId, docStatus);
        List<KnowledgeDocumentResponse> responses = documents.stream()
                .map(this::toDocumentResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<KnowledgeDocumentResponse> getDocument(@PathVariable String id) {
        KnowledgeDocument document = documentIngestionService.getDocument(KnowledgeDocumentId.fromString(id));
        return ResponseEntity.ok(toDocumentResponse(document));
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentIngestionService.deleteDocument(KnowledgeDocumentId.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/documents/{id}/chunk")
    public ResponseEntity<List<ChunkResponse>> chunkDocument(
            @PathVariable String id, @RequestBody ChunkRequest request) {
        log.info("Chunking document: {}", id);
        ChunkStrategy strategy = request.getStrategy() != null
                ? ChunkStrategy.valueOf(request.getStrategy().toUpperCase())
                : ChunkStrategy.FIXED_SIZE;
        List<ChunkResult> results = documentIngestionService.chunkDocument(
                KnowledgeDocumentId.fromString(id), strategy,
                request.getMaxChunkSize(), request.getOverlap());
        List<ChunkResponse> responses = results.stream().map(this::toChunkResponse).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PostMapping("/embed")
    public ResponseEntity<EmbedResponse> embed(@RequestBody EmbedRequest request) {
        log.info("Generating embedding");
        EmbeddingProvider provider = request.getProvider() != null
                ? EmbeddingProvider.valueOf(request.getProvider().toUpperCase())
                : EmbeddingProvider.OPENAI;
        List<Float> embedding = embeddingService.embed(request.getText(), provider);
        EmbedResponse response = new EmbedResponse();
        response.setEmbedding(embedding);
        response.setDimensions(embedding.size());
        response.setProvider(provider.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/index")
    public ResponseEntity<Void> indexDocument(@RequestBody IndexRequest request) {
        log.info("Indexing document: {}", request.getDocumentId());
        EmbeddingProvider embedProvider = request.getEmbeddingProvider() != null
                ? EmbeddingProvider.valueOf(request.getEmbeddingProvider().toUpperCase())
                : EmbeddingProvider.OPENAI;
        VectorStoreProvider vectorStore = request.getVectorStoreProvider() != null
                ? VectorStoreProvider.valueOf(request.getVectorStoreProvider().toUpperCase())
                : VectorStoreProvider.PGVECTOR;
        embeddingService.indexDocument(
                KnowledgeDocumentId.fromString(request.getDocumentId()), embedProvider, vectorStore);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reindex")
    public ResponseEntity<Void> reindexAll() {
        log.info("Reindexing all documents");
        embeddingService.reindexAll(EmbeddingProvider.OPENAI, VectorStoreProvider.PGVECTOR);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/query")
    public ResponseEntity<QueryResponse> query(@RequestBody QueryRequest request) {
        log.info("Executing query: {}", request.getQuery());
        RetrievalStrategy strategy = request.getStrategy() != null
                ? RetrievalStrategy.valueOf(request.getStrategy().toUpperCase())
                : RetrievalStrategy.HYBRID;
        List<RetrievalResult> results = semanticRetrievalService.search(
                request.getQuery(), request.getTopK(), strategy,
                request.getWorkspaceId(), request.getFilters());
        return ResponseEntity.ok(toQueryResponse(request.getQuery(), results, strategy.name()));
    }

    @PostMapping("/search")
    public ResponseEntity<QueryResponse> hybridSearch(@RequestBody SearchRequest request) {
        log.info("Executing hybrid search: {}", request.getQuery());
        List<RetrievalResult> results = semanticRetrievalService.hybridSearch(
                request.getQuery(), request.getTopK(), request.getWorkspaceId(),
                request.getVectorWeight(), request.getFilters());
        return ResponseEntity.ok(toQueryResponse(request.getQuery(), results, "HYBRID"));
    }

    @GetMapping("/chunks")
    public ResponseEntity<List<ChunkResponse>> getChunks(
            @RequestParam String documentId) {
        List<KnowledgeChunk> chunks = documentIngestionService.getChunks(
                KnowledgeDocumentId.fromString(documentId));
        List<ChunkResponse> responses = chunks.stream()
                .map(c -> {
                    ChunkResponse cr = new ChunkResponse();
                    cr.setChunkId(c.id().toString());
                    cr.setDocumentId(c.documentId().toString());
                    cr.setContent(truncate(c.content(), 200));
                    cr.setChunkIndex(c.chunkIndex());
                    cr.setSequence(c.chunkIndex());
                    cr.setTokens(c.tokens());
                    return cr;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/context")
    public ResponseEntity<ContextResponse> buildContext(@RequestBody ContextRequest request) {
        log.info("Building context for query: {}", request.getQuery());
        RetrievalStrategy strategy = request.getStrategy() != null
                ? RetrievalStrategy.valueOf(request.getStrategy().toUpperCase())
                : RetrievalStrategy.HYBRID;
        List<RetrievalResult> results = semanticRetrievalService.search(
                request.getQuery(), request.getTopK(), strategy,
                request.getWorkspaceId(), request.getFilters());
        RagContext ragContext = contextBuilderService.buildContext(
                request.getQuery(), results, request.getMaxTokens());
        List<Citation> citations = citationGenerator.generateCitations(request.getQuery(), results);

        ContextResponse response = new ContextResponse();
        response.setQuery(request.getQuery());
        response.setContext(ragContext.assembledContext());
        response.setTotalTokens(ragContext.totalTokens());
        response.setMaxTokens(ragContext.maxTokens());
        response.setSources(ragContext.sources());
        response.setCitations(citations.stream().map(c -> {
                        ContextResponse.CitationInfo ci = new ContextResponse.CitationInfo();
                            ci.setCitationId(c.chunkId() != null ? c.chunkId().value().toString() : UUID.randomUUID().toString());
                            ci.setDocumentId(c.documentId() != null ? c.documentId().value().toString() : null);
                            ci.setSource(c.source());
                            ci.setExcerpt(c.excerpt());
                            ci.setRelevanceScore(c.relevanceScore());
            return ci;
        }).collect(Collectors.toList()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rag")
    public ResponseEntity<RagQueryResponse> executeRagPipeline(@RequestBody RagQueryRequest request) {
        log.info("Executing RAG pipeline for query: {}", request.getQuery());
        RetrievalStrategy strategy = request.getStrategy() != null
                ? RetrievalStrategy.valueOf(request.getStrategy().toUpperCase())
                : RetrievalStrategy.HYBRID;
        RagContext ragContext = ragOrchestratorService.executeQuery(
                request.getQuery(), request.getWorkspaceId(), request.getTopK(),
                strategy, request.getMaxContextTokens(), EmbeddingProvider.OPENAI, request.getFilters());
        List<Citation> citations = citationGenerator.generateCitations(
                request.getQuery(), ragContext.results());

        RagQueryResponse response = new RagQueryResponse();
        response.setQuery(request.getQuery());

        RagQueryResponse.RagContextInfo ctxInfo = new RagQueryResponse.RagContextInfo();
        ctxInfo.setAssembledContext(ragContext.assembledContext());
        ctxInfo.setTotalTokens(ragContext.totalTokens());
        ctxInfo.setMaxTokens(ragContext.maxTokens());
        ctxInfo.setSources(ragContext.sources());
        response.setContext(ctxInfo);

        response.setCitations(citations.stream().map(c -> {
            RagQueryResponse.RagCitation rc = new RagQueryResponse.RagCitation();
            rc.setCitationId(c.chunkId() != null ? c.chunkId().value().toString() : UUID.randomUUID().toString());
            rc.setDocumentId(c.documentId() != null ? c.documentId().value().toString() : null);
            rc.setSource(c.source());
            rc.setExcerpt(c.excerpt());
            rc.setRelevanceScore(c.relevanceScore());
            return rc;
        }).collect(Collectors.toList()));

        response.setTotalResults(ragContext.results().size());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/collections")
    public ResponseEntity<KnowledgeCollectionResponse> createCollection(@RequestBody CreateCollectionRequest request) {
        log.info("Creating collection: {}", request.getName());
        KnowledgeCollection collection = knowledgeRegistryService.createCollection(
                request.getName(), request.getDescription(),
                request.getWorkspaceId(), request.getOwner());
        return ResponseEntity.status(HttpStatus.CREATED).body(toCollectionResponse(collection));
    }

    @GetMapping("/collections")
    public ResponseEntity<List<KnowledgeCollectionResponse>> listCollections(
            @RequestParam(required = false) String workspaceId) {
        List<KnowledgeCollection> collections = knowledgeRegistryService.listCollections(workspaceId);
        List<KnowledgeCollectionResponse> responses = collections.stream()
                .map(this::toCollectionResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = knowledgeMetricsService.getMetrics();
        return ResponseEntity.ok(metrics);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        Map<String, String> body = new HashMap<>();
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        Map<String, String> body = new HashMap<>();
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleInternalError(Exception ex) {
        log.error("Internal server error", ex);
        Map<String, String> body = new HashMap<>();
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private KnowledgeSourceResponse toSourceResponse(KnowledgeSource source) {
        KnowledgeSourceResponse resp = new KnowledgeSourceResponse();
        resp.setId(source.id().value().toString());
        resp.setName(source.name());
        resp.setDescription(source.description());
        resp.setWorkspaceId(source.workspaceId());
        resp.setOwner(source.owner());
        resp.setSourceType(source.sourceType().name());
        resp.setSourceUrl(source.sourceUrl());
        resp.setVersion(source.version());
        resp.setStatus(source.status().name());
        resp.setLanguage(source.language());
        resp.setTags(source.tags());
        resp.setRetentionDays(source.retentionDays());
        resp.setCreatedAt(source.createdAt());
        resp.setUpdatedAt(source.updatedAt());
        return resp;
    }

    private KnowledgeDocumentResponse toDocumentResponse(KnowledgeDocument document) {
        KnowledgeDocumentResponse resp = new KnowledgeDocumentResponse();
        resp.setId(document.id().toString());
        resp.setSourceId(document.sourceId().toString());
        resp.setTitle(document.title());
        resp.setDocumentType(document.documentType().name());
        resp.setContent(truncate(document.content(), 500));
        resp.setVersion(document.version());
        resp.setStatus(document.status().name());
        resp.setChecksum(document.checksum());
        resp.setFileSize(document.fileSize());
        resp.setAuthor(document.author());
        resp.setLanguage(document.language());
        resp.setChunkCount(document.chunkCount());
        resp.setCreatedAt(document.createdAt());
        resp.setUpdatedAt(document.updatedAt());
        return resp;
    }

    private ChunkResponse toChunkResponse(ChunkResult result) {
        ChunkResponse resp = new ChunkResponse();
        resp.setChunkId(result.chunkId().value().toString());
        resp.setContent(truncate(result.content(), 200));
        resp.setSequence(result.sequence());
        resp.setChunkIndex(result.sequence());
        resp.setTokens(result.tokens());
        resp.setHeading(result.heading());
        resp.setSection(result.section());
        return resp;
    }

    private KnowledgeCollectionResponse toCollectionResponse(KnowledgeCollection collection) {
        KnowledgeCollectionResponse resp = new KnowledgeCollectionResponse();
        resp.setId(collection.id().value().toString());
        resp.setName(collection.name());
        resp.setDescription(collection.description());
        resp.setWorkspaceId(collection.workspaceId());
        resp.setOwner(collection.owner());
        resp.setDefaultPermission(collection.defaultPermission().name());
        resp.setTags(collection.tags());
        resp.setPolicy(collection.policy());
        resp.setCreatedAt(collection.createdAt());
        resp.setUpdatedAt(collection.updatedAt());
        return resp;
    }

    private QueryResponse toQueryResponse(String query, List<RetrievalResult> results, String strategy) {
        QueryResponse resp = new QueryResponse();
        resp.setQuery(query);
        resp.setStrategy(strategy);
        resp.setTotalResults(results.size());
        resp.setResults(results.stream().map(r -> {
            QueryResponse.SearchResultItem item = new QueryResponse.SearchResultItem();
            item.setChunkId(r.chunk().id().toString());
            item.setDocumentId(r.chunk().documentId().toString());
            item.setContent(truncate(r.chunk().content(), 200));
            item.setScore(r.score());
            item.setChunkIndex(r.chunk().chunkIndex());
            item.setSection(r.chunk().section());
            if (r.citation() != null) {
                item.setCitationId(r.citation().chunkId() != null ? r.citation().chunkId().toString() : null);
                item.setCitationSource(r.citation().source());
                item.setCitationConfidence(r.citation().confidence());
                item.setCitationExcerpt(r.citation().excerpt());
            }
            return item;
        }).collect(Collectors.toList()));
        return resp;
    }

    private static String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
