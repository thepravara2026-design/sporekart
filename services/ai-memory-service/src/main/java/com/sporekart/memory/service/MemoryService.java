package com.sporekart.memory.service;

import com.sporekart.memory.consolidation.ConsolidationService;
import com.sporekart.memory.domain.MemoryType;
import com.sporekart.memory.domain.Priority;
import com.sporekart.memory.domain.RetentionPolicy;
import com.sporekart.memory.domain.Visibility;
import com.sporekart.memory.dto.CreateMemoryRequest;
import com.sporekart.memory.dto.MemoryResponse;
import com.sporekart.memory.dto.UpdateMemoryRequest;
import com.sporekart.memory.embedding.InMemoryEmbeddingProvider;
import com.sporekart.memory.embedding.InMemoryVectorStore;
import com.sporekart.memory.event.MemoryEventPublisher;
import com.sporekart.memory.persistence.MemoryEntity;
import com.sporekart.memory.repository.MemoryChunkRepository;
import com.sporekart.memory.repository.MemoryRepository;
import com.sporekart.memory.repository.MemorySummaryRepository;
import com.sporekart.memory.repository.MemoryTagRepository;
import com.sporekart.memory.retrieval.ContextBuilder;
import com.sporekart.memory.retrieval.RetrievalQuery;
import com.sporekart.memory.retrieval.RetrievalService;
import com.sporekart.memory.summarization.SummarizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemoryService {

    private static final Logger log = LoggerFactory.getLogger(MemoryService.class);

    private final MemoryRepository memoryRepository;
    private final MemoryTagRepository tagRepository;
    private final MemoryChunkRepository chunkRepository;
    private final MemorySummaryRepository summaryRepository;
    private final RetrievalService retrievalService;
    private final ContextBuilder contextBuilder;
    private final SummarizationService summarizationService;
    private final ConsolidationService consolidationService;
    private final InMemoryEmbeddingProvider embeddingProvider;
    private final InMemoryVectorStore vectorStore;
    private final MemoryEventPublisher eventPublisher;

    public MemoryService(MemoryRepository memoryRepository,
                         MemoryTagRepository tagRepository,
                         MemoryChunkRepository chunkRepository,
                         MemorySummaryRepository summaryRepository,
                         RetrievalService retrievalService,
                         ContextBuilder contextBuilder,
                         SummarizationService summarizationService,
                         ConsolidationService consolidationService,
                         InMemoryEmbeddingProvider embeddingProvider,
                         InMemoryVectorStore vectorStore,
                         MemoryEventPublisher eventPublisher) {
        this.memoryRepository = memoryRepository;
        this.tagRepository = tagRepository;
        this.chunkRepository = chunkRepository;
        this.summaryRepository = summaryRepository;
        this.retrievalService = retrievalService;
        this.contextBuilder = contextBuilder;
        this.summarizationService = summarizationService;
        this.consolidationService = consolidationService;
        this.embeddingProvider = embeddingProvider;
        this.vectorStore = vectorStore;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public MemoryResponse create(CreateMemoryRequest request) {
        log.info("Creating memory: {}", request.title());

        var entity = new MemoryEntity();
        entity.setTitle(request.title());
        entity.setContent(request.content());
        entity.setMemoryType(request.memoryType() != null ? request.memoryType() : MemoryType.NOTE);
        entity.setSource(request.source() != null ? request.source() : "MANUAL");
        entity.setPriority(request.priority() != null ? request.priority() : Priority.MEDIUM);
        entity.setImportance(request.importance() != null ? request.importance() : 5);
        entity.setOwnerId(request.ownerId());
        entity.setOwnerType(request.ownerType() != null ? request.ownerType() : "USER");
        entity.setWorkspace(request.workspace() != null ? request.workspace() : "default");
        entity.setDepartment(request.department());
        entity.setVisibility(request.visibility() != null ? request.visibility() : Visibility.PRIVATE);
        entity.setEntityId(request.entityId());
        entity.setEntityType(request.entityType());
        entity.setConversationId(request.conversationId());
        entity.setIsEphemeral(request.isEphemeral() != null && request.isEphemeral());
        entity.setHasEncryption(request.hasEncryption() != null && request.hasEncryption());
        entity.setIsDeleted(false);
        entity.setAccessCount(0);
        entity.setCreatedBy(request.ownerId());
        entity.setRetentionPolicy(RetentionPolicy.STANDARD);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setLastAccessedAt(OffsetDateTime.now());

        if (request.expiresIn() != null) {
            entity.setExpiresAt(OffsetDateTime.now().plus(
                    request.expiresIn().amount(),
                    switch (request.expiresIn().unit().toUpperCase()) {
                        case "S" -> ChronoUnit.SECONDS;
                        case "M" -> ChronoUnit.MINUTES;
                        case "H" -> ChronoUnit.HOURS;
                        case "D" -> ChronoUnit.DAYS;
                        case "W" -> ChronoUnit.WEEKS;
                        default -> ChronoUnit.HOURS;
                    }
            ));
        }

        if (request.tags() != null) {
            request.tags().forEach(tagName -> {
                var tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            var newTag = new com.sporekart.memory.persistence.MemoryTagEntity();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        });
                entity.getTags().add(tag);
            });
        }

        var saved = memoryRepository.save(entity);

        generateAndStoreEmbedding(saved);

        if (request.content() != null && !request.content().isBlank()) {
            summarizationService.summarize(saved);
        }

        eventPublisher.publishMemoryCreated(saved);

        log.info("Memory created with id: {}", saved.getId());
        return mapToResponse(saved);
    }

    public Optional<MemoryResponse> findById(UUID id) {
        return memoryRepository.findByIdAndIsDeletedFalse(id)
                .map(this::mapToAccess);
    }

    public List<MemoryResponse> findByOwner(UUID ownerId) {
        return memoryRepository.findByOwnerIdAndIsDeletedFalse(ownerId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MemoryResponse> findByWorkspace(String workspace) {
        return memoryRepository.findByWorkspaceAndIsDeletedFalse(workspace).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<MemoryResponse> findAll() {
        return memoryRepository.findByIsDeletedFalse().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public Optional<MemoryResponse> update(UUID id, UpdateMemoryRequest request) {
        return memoryRepository.findByIdAndIsDeletedFalse(id)
                .map(entity -> {
                    if (request.title() != null) entity.setTitle(request.title());
                    if (request.content() != null) entity.setContent(request.content());
                    if (request.memoryType() != null) entity.setMemoryType(request.memoryType());
                    if (request.source() != null) entity.setSource(request.source());
                    if (request.priority() != null) entity.setPriority(request.priority());
                    if (request.importance() != null) entity.setImportance(request.importance());
        if (request.metadata() != null && !request.metadata().isEmpty()) {
            entity.setMetadata(request.metadata());
        }

        if (request.tags() != null) {
                        entity.getTags().clear();
                        request.tags().forEach(tagName -> {
                    var tag = tagRepository.findByName(tagName)
                            .orElseGet(() -> {
                                var newTag = new com.sporekart.memory.persistence.MemoryTagEntity();
                                newTag.setName(tagName);
                                return tagRepository.save(newTag);
                            });
                            entity.getTags().add(tag);
                        });
                    }
                    entity.setUpdatedAt(OffsetDateTime.now());

                    var saved = memoryRepository.save(entity);

                    generateAndStoreEmbedding(saved);
                    eventPublisher.publishMemoryUpdated(saved);

                    return mapToResponse(saved);
                });
    }

    @Transactional
    public boolean delete(UUID id) {
        return memoryRepository.findByIdAndIsDeletedFalse(id)
                .map(entity -> {
                    entity.setIsDeleted(true);
                    entity.setUpdatedAt(OffsetDateTime.now());
                    memoryRepository.save(entity);
                    eventPublisher.publishMemoryDeleted(id);
                    log.info("Soft-deleted memory: {}", id);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean hardDelete(UUID id) {
        return memoryRepository.findById(id)
                .map(entity -> {
                    chunkRepository.deleteByMemoryId(id);
                    summaryRepository.deleteAll(summaryRepository.findByMemoryIdOrderByCreatedAtDesc(id));
                    memoryRepository.delete(entity);
                    eventPublisher.publishMemoryDeleted(id);
                    log.info("Hard-deleted memory: {}", id);
                    return true;
                })
                .orElse(false);
    }

    public List<com.sporekart.memory.retrieval.RetrievedMemory> search(com.sporekart.memory.dto.SearchMemoriesRequest request) {
        var query = RetrievalQuery.builder()
                .naturalLanguageQuery(request.query())
                .tags(request.tags())
                .workspaces(request.workspaces())
                .memoryTypes(request.memoryTypes())
                .sources(request.sources())
                .maxResults(request.maxResults() > 0 ? request.maxResults() : 20)
                .minRelevanceScore(request.minRelevanceScore())
                .build();

        return retrievalService.retrieve(query);
    }

    public com.sporekart.memory.retrieval.RetrievalContext searchWithContext(com.sporekart.memory.dto.SearchMemoriesRequest request) {
        var memories = search(request);
        return contextBuilder.buildContext(request.query(), memories);
    }

    public String formatForPrompt(com.sporekart.memory.dto.SearchMemoriesRequest request, int maxTokens) {
        var memories = search(request);
        return contextBuilder.formatForPrompt(memories, maxTokens);
    }

    public long count() {
        return memoryRepository.countByIsDeletedFalse();
    }

    public long countByWorkspace(String workspace) {
        return memoryRepository.countByWorkspaceAndIsDeletedFalse(workspace);
    }

    public long countByOwner(UUID ownerId) {
        return memoryRepository.countByOwnerIdAndIsDeletedFalse(ownerId);
    }

    private void generateAndStoreEmbedding(MemoryEntity memory) {
        try {
            var textToEmbed = (memory.getTitle() != null ? memory.getTitle() + " " : "")
                    + (memory.getContent() != null ? memory.getContent() : "");
            if (!textToEmbed.isBlank()) {
                var embedding = embeddingProvider.generateEmbedding(textToEmbed);
                vectorStore.store(memory.getId(), embedding.vector(), memory.getTitle());
            }
        } catch (Exception e) {
            log.warn("Failed to generate embedding for memory {}: {}", memory.getId(), e.getMessage());
        }
    }

    private MemoryResponse mapToResponse(MemoryEntity entity) {
        return new MemoryResponse(
                entity.getId(), entity.getTitle(), entity.getContent(), entity.getSummary(),
                entity.getMemoryType(), entity.getSource(), entity.getPriority(),
                entity.getImportance(),
                entity.getTags().stream().map(t -> t.getName()).toList(),
                entity.getOwnerId(), entity.getOwnerType(), entity.getWorkspace(),
                entity.getDepartment(), entity.getVisibility(),
                entity.getEntityId(), entity.getEntityType(), entity.getConversationId(),
                entity.getIsEphemeral(), entity.getExpiresAt(), entity.getHasEncryption(),
                entity.getMetadata() != null ? entity.getMetadata() : Map.of(),
                entity.getCreatedAt(), entity.getUpdatedAt(), entity.getLastAccessedAt(),
                entity.getAccessCount()
        );
    }

    private MemoryResponse mapToAccess(MemoryEntity entity) {
        entity.setAccessCount(entity.getAccessCount() + 1);
        entity.setLastAccessedAt(OffsetDateTime.now());
        memoryRepository.save(entity);
        return mapToResponse(entity);
    }
}
