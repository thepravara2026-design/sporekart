package com.sporekart.ai.semantic.interfaces.rest;

import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.semantic.application.*;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.*;
import com.sporekart.ai.semantic.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/semantic")
@Tag(name = "Semantic Intelligence", description = "Semantic search, embeddings, and vector index management APIs")
public class SemanticController {

    private static final Logger log = LoggerFactory.getLogger(SemanticController.class);

    private final SemanticEmbeddingService embeddingService;
    private final SemanticSearchServiceImpl searchService;
    private final SemanticIndexService indexService;
    private final SemanticEmbeddingBatchService batchService;
    private final SemanticRedisCacheService cacheService;

    public SemanticController(SemanticEmbeddingService embeddingService,
                              SemanticSearchServiceImpl searchService,
                              SemanticIndexService indexService,
                              SemanticEmbeddingBatchService batchService,
                              SemanticRedisCacheService cacheService) {
        this.embeddingService = embeddingService;
        this.searchService = searchService;
        this.indexService = indexService;
        this.batchService = batchService;
        this.cacheService = cacheService;
    }

    @PostMapping("/embed")
    @Operation(summary = "Create an embedding")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Embedding created"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<EmbedResponse>> createEmbedding(
            @Valid @RequestBody EmbedRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000000") UUID userId) {
        var entity = embeddingService.createEmbedding(
                request.content(),
                null,
                request.provider(),
                request.model(),
                0,
                "PENDING",
                userId);
        EmbedResponse response = new EmbedResponse(
                entity.getId().toString(),
                entity.getStatus(),
                entity.getDimensions() != null ? entity.getDimensions() : 0,
                entity.getProvider(),
                entity.getModel(),
                entity.getCreatedAt().toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseEnvelope.ok(response));
    }

    @PostMapping("/search")
    @Operation(summary = "Execute a semantic search")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<SearchResponse>> search(
            @Valid @RequestBody SearchRequest request) {
        long start = System.currentTimeMillis();
        var type = com.sporekart.ai.semantic.domain.SearchType.valueOf(request.type().toUpperCase());
        var results = searchService.search(request.query(), type, request.filters(),
                request.limit(), request.threshold());
        long latency = System.currentTimeMillis() - start;

        var items = results.stream()
                .map(r -> new SearchResultItem(r.documentId(), r.content(), r.score(), r.rank(), r.metadata()))
                .collect(Collectors.toList());

        SearchResponse response = new SearchResponse(items, results.size(), request.type(), latency);
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @PostMapping("/similarity")
    @Operation(summary = "Find similar embeddings")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Similarity search completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<SimilarityResponse>> similarity(
            @Valid @RequestBody SimilarityRequest request) {
        long start = System.currentTimeMillis();
        var results = searchService.similaritySearch(request.embeddingId(),
                request.limit(), request.threshold());
        long latency = System.currentTimeMillis() - start;

        var items = results.stream()
                .map(r -> new SimilarityResultItem(r.documentId(), r.score(), r.content()))
                .collect(Collectors.toList());

        SimilarityResponse response = new SimilarityResponse(items, request.embeddingId(),
                request.algorithm(), latency);
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @GetMapping("/index")
    @Operation(summary = "List all vector indexes")
    public ResponseEntity<ResponseEnvelope<java.util.List<IndexResponse>>> listIndexes() {
        var indexes = indexService.listIndexes().stream()
                .map(idx -> new IndexResponse(
                        idx.getId().toString(),
                        idx.getName(),
                        idx.getStatus(),
                        idx.getVectorCount(),
                        idx.getDimensions(),
                        idx.getCreatedAt().toString()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(indexes));
    }

    @PostMapping("/index/rebuild")
    @Operation(summary = "Rebuild a vector index")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Index rebuild initiated"),
        @ApiResponse(responseCode = "404", description = "Index not found", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<IndexResponse>> rebuildIndex(@RequestParam String name) {
        indexService.rebuildIndex(name);
        var idx = indexService.getIndexByName(name);
        IndexResponse response = new IndexResponse(
                idx.getId().toString(), idx.getName(), idx.getStatus(),
                idx.getVectorCount(), idx.getDimensions(), idx.getCreatedAt().toString());
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get index statistics")
    public ResponseEntity<ResponseEnvelope<StatisticsResponse>> getStatistics(
            @RequestParam(defaultValue = "default") String indexName) {
        var stats = indexService.getIndexStatistics(indexName);
        StatisticsResponse response = new StatisticsResponse(indexName, stats, OffsetDateTime.now().toString());
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check for semantic platform")
    public ResponseEntity<ResponseEnvelope<HealthResponse>> health() {
        long start = System.currentTimeMillis();
        long embeddingCount = embeddingService.listEmbeddings().size();
        long latency = System.currentTimeMillis() - start;

        HealthResponse response = new HealthResponse(
                "UP",
                indexService.listIndexes().stream()
                        .map(SemanticVectorIndexEntity::getStatus)
                        .collect(Collectors.joining(",")),
                embeddingCount,
                0.0,
                latency);
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @ExceptionHandler(EmbeddingException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleEmbedding(EmbeddingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseEnvelope.error("SEMANTIC-400", ex.getMessage()));
    }

    @ExceptionHandler(IndexException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleIndex(IndexException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseEnvelope.error("SEMANTIC-404", ex.getMessage()));
    }

    @ExceptionHandler(SearchException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleSearch(SearchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseEnvelope.error("SEMANTIC-400", ex.getMessage()));
    }
}
