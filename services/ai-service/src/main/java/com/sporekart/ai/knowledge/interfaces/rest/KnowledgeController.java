package com.sporekart.ai.knowledge.interfaces.rest;

import com.sporekart.ai.core.domain.ResponseEnvelope;
import com.sporekart.ai.knowledge.application.*;
import com.sporekart.ai.knowledge.domain.DocumentStatus;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import com.sporekart.ai.knowledge.interfaces.rest.dto.*;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/knowledge")
@Tag(name = "Knowledge Platform", description = "Enterprise Knowledge Platform & RAG Foundation APIs")
public class KnowledgeController {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeController.class);

    private final KnowledgeDocumentService documentService;
    private final KnowledgeChunkingService chunkingService;
    private final KnowledgeMetadataService metadataService;
    private final KnowledgeRetrievalService retrievalService;
    private final KnowledgeSecurityService securityService;
    private final KnowledgeRedisCacheService cacheService;
    private final KnowledgeKafkaEventPublisher kafkaPublisher;

    public KnowledgeController(KnowledgeDocumentService documentService,
                               KnowledgeChunkingService chunkingService,
                               KnowledgeMetadataService metadataService,
                               KnowledgeRetrievalService retrievalService,
                               KnowledgeSecurityService securityService,
                               KnowledgeRedisCacheService cacheService,
                               KnowledgeKafkaEventPublisher kafkaPublisher) {
        this.documentService = documentService;
        this.chunkingService = chunkingService;
        this.metadataService = metadataService;
        this.retrievalService = retrievalService;
        this.securityService = securityService;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @PostMapping("/documents")
    @Operation(summary = "Create a knowledge document")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Document created"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<DocumentResponse>> createDocument(
            @Valid @RequestBody CreateDocumentRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000000") UUID userId) {
        KnowledgeDocumentEntity doc = documentService.createDocument(
                request.categoryId(),
                request.title(),
                request.content(),
                request.description(),
                request.language(),
                request.author(),
                request.source(),
                request.visibility(),
                request.businessModule(),
                request.region(),
                request.tags(),
                userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseEnvelope.ok(DocumentResponse.from(doc)));
    }

    @GetMapping("/documents")
    @Operation(summary = "List all knowledge documents")
    public ResponseEntity<ResponseEnvelope<List<DocumentResponse>>> listDocuments() {
        List<DocumentResponse> docs = documentService.listDocuments().stream()
                .map(DocumentResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(docs));
    }

    @GetMapping("/documents/{id}")
    @Operation(summary = "Get a knowledge document by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Document found"),
        @ApiResponse(responseCode = "404", description = "Document not found", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<DocumentResponse>> getDocument(@PathVariable UUID id) {
        KnowledgeDocumentEntity doc = documentService.getDocument(id);
        return ResponseEntity.ok(ResponseEnvelope.ok(DocumentResponse.from(doc)));
    }

    @PutMapping("/documents/{id}")
    @Operation(summary = "Update a knowledge document")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Document updated"),
        @ApiResponse(responseCode = "404", description = "Document not found", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<DocumentResponse>> updateDocument(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDocumentRequest request,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000000") UUID userId) {
        KnowledgeDocumentEntity doc = documentService.updateDocument(
                id, request.categoryId(), request.title(), request.content(),
                request.description(), request.language(), request.source(),
                request.visibility(), request.businessModule(), request.region(), userId);
        return ResponseEntity.ok(ResponseEnvelope.ok(DocumentResponse.from(doc)));
    }

    @DeleteMapping("/documents/{id}")
    @Operation(summary = "Delete a knowledge document")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Document deleted"),
        @ApiResponse(responseCode = "404", description = "Document not found", content = @Content)
    })
    public ResponseEntity<ResponseEnvelope<Void>> deleteDocument(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000000") UUID userId) {
        documentService.deleteDocument(id, userId);
        return ResponseEntity.ok(ResponseEnvelope.ok(null));
    }

    @GetMapping("/categories")
    @Operation(summary = "List knowledge categories")
    public ResponseEntity<ResponseEnvelope<List<CategoryResponse>>> listCategories() {
        List<CategoryResponse> categories = metadataService.listCategories().stream()
                .map(CategoryResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(categories));
    }

    @GetMapping("/search")
    @Operation(summary = "Search knowledge documents")
    public ResponseEntity<ResponseEnvelope<List<DocumentResponse>>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String businessModule) {
        List<KnowledgeDocumentEntity> results;
        if (category != null && !category.isBlank()) {
            results = documentService.findByCategory(UUID.fromString(category));
        } else if (businessModule != null && !businessModule.isBlank()) {
            results = documentService.findByBusinessModule(businessModule);
        } else {
            results = documentService.searchDocuments(q);
        }
        List<DocumentResponse> docs = results.stream().map(DocumentResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(docs));
    }

    @PostMapping("/retrieve")
    @Operation(summary = "Retrieve knowledge context for AI")
    public ResponseEntity<ResponseEnvelope<RetrieveResponse>> retrieve(
            @Valid @RequestBody RetrieveRequest request) {
        KnowledgeRetrievalService.RetrievalResult result = retrievalService.retrieve(
                request.query(), request.categories(), request.language(),
                request.visibility(), request.businessModule(), request.maxChunks());
        RetrieveResponse response = new RetrieveResponse(
                result.requestId(),
                result.documents().stream().map(DocumentResponse::from).collect(Collectors.toList()),
                result.chunks().stream().map(ChunkResponse::from).collect(Collectors.toList()),
                result.citations().stream().map(CitationResponse::from).collect(Collectors.toList()));
        return ResponseEntity.ok(ResponseEnvelope.ok(response));
    }

    @GetMapping("/citations")
    @Operation(summary = "Get citations by retrieval request ID")
    public ResponseEntity<ResponseEnvelope<List<CitationResponse>>> getCitations(
            @RequestParam UUID requestId) {
        List<CitationResponse> citations = retrievalService.getCitations(requestId).stream()
                .map(CitationResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(citations));
    }

    @PostMapping("/documents/{id}/chunk")
    @Operation(summary = "Chunk a document into smaller pieces")
    public ResponseEntity<ResponseEnvelope<List<ChunkResponse>>> chunkDocument(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "1000") int chunkSize,
            @RequestParam(defaultValue = "100") int overlap) {
        List<ChunkResponse> chunks = chunkingService.chunkDocument(id, chunkSize, overlap).stream()
                .map(ChunkResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(chunks));
    }

    @GetMapping("/documents/{id}/chunks")
    @Operation(summary = "Get chunks for a document")
    public ResponseEntity<ResponseEnvelope<List<ChunkResponse>>> getChunks(@PathVariable UUID id) {
        List<ChunkResponse> chunks = chunkingService.getChunks(id).stream()
                .map(ChunkResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(ResponseEnvelope.ok(chunks));
    }

    @PostMapping("/documents/{id}/publish")
    @Operation(summary = "Publish a knowledge document")
    public ResponseEntity<ResponseEnvelope<DocumentResponse>> publishDocument(
            @PathVariable UUID id,
            @RequestHeader(value = "X-User-Id", defaultValue = "00000000-0000-0000-0000-000000000000") UUID userId) {
        KnowledgeDocumentEntity doc = documentService.publishDocument(id, userId);
        return ResponseEntity.ok(ResponseEnvelope.ok(DocumentResponse.from(doc)));
    }

    @ExceptionHandler(KnowledgeNotFoundException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleNotFound(KnowledgeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseEnvelope.error("KNOWLEDGE-404", ex.getMessage()));
    }

    @ExceptionHandler(KnowledgeValidationException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleValidation(KnowledgeValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseEnvelope.error("KNOWLEDGE-400", ex.getMessage()));
    }

    @ExceptionHandler(KnowledgeSecurityException.class)
    public ResponseEntity<ResponseEnvelope<Void>> handleSecurity(KnowledgeSecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseEnvelope.error("KNOWLEDGE-403", ex.getMessage()));
    }
}
