package com.sporekart.memory.retrieval;

import com.sporekart.memory.embedding.InMemoryEmbeddingProvider;
import com.sporekart.memory.embedding.InMemoryVectorStore;
import com.sporekart.memory.persistence.MemoryEntity;
import com.sporekart.memory.repository.MemoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private static final Logger log = LoggerFactory.getLogger(RetrievalService.class);

    private final MemoryRepository memoryRepository;
    private final InMemoryEmbeddingProvider embeddingProvider;
    private final InMemoryVectorStore vectorStore;

    public RetrievalService(MemoryRepository memoryRepository,
                            InMemoryEmbeddingProvider embeddingProvider,
                            InMemoryVectorStore vectorStore) {
        this.memoryRepository = memoryRepository;
        this.embeddingProvider = embeddingProvider;
        this.vectorStore = vectorStore;
    }

    public List<RetrievedMemory> retrieve(RetrievalQuery query) {
        log.info("Executing retrieval query: {}", query.naturalLanguageQuery());

        var results = new HashSet<UUID>();
        var scoreMap = new HashMap<UUID, Double>();

        if (query.useSemanticSearch() && query.naturalLanguageQuery() != null && !query.naturalLanguageQuery().isBlank()) {
            var semanticResults = semanticSearch(query);
            semanticResults.forEach(r -> {
                results.add(r.memoryId());
                scoreMap.merge(r.memoryId(), r.relevanceScore(), Math::max);
            });
        }

        if (query.useKeywordSearch() && query.naturalLanguageQuery() != null && !query.naturalLanguageQuery().isBlank()) {
            var keywordResults = keywordSearch(query);
            keywordResults.forEach(r -> {
                results.add(r.memoryId());
                scoreMap.merge(r.memoryId(), r.relevanceScore(), Math::max);
            });
        }

        if (query.useMetadataSearch()) {
            var metadataResults = metadataSearch(query);
            metadataResults.forEach(r -> {
                results.add(r.memoryId());
                scoreMap.merge(r.memoryId(), r.relevanceScore(), Math::max);
            });
        }

        var retrieved = results.stream()
                .map(id -> memoryRepository.findByIdAndIsDeletedFalse(id).orElse(null))
                .filter(Objects::nonNull)
                .filter(m -> query.includeExpired() || m.getExpiresAt() == null || m.getExpiresAt().isAfter(OffsetDateTime.now()))
                .filter(m -> query.minRelevanceScore() == 0 || scoreMap.getOrDefault(m.getId(), 0.0) >= query.minRelevanceScore())
                .sorted(Comparator.comparingDouble((MemoryEntity m) -> scoreMap.getOrDefault(m.getId(), 0.0)).reversed())
                .limit(query.maxResults())
                .map(m -> new RetrievedMemory(
                        m.getId(), m.getTitle(), m.getContent(), m.getSummary(),
                        m.getMemoryType(), scoreMap.getOrDefault(m.getId(), 0.0),
                        m.getSource(), m.getPriority(), m.getImportance(),
                        m.getTags().stream().map(t -> t.getName()).toList(),
                        m.getOwnerId(), m.getOwnerType(), m.getWorkspace(),
                        m.getCreatedAt(), m.getLastAccessedAt(),
                        m.getIsEphemeral(), m.getHasEncryption()))
                .toList();

        log.info("Retrieval returned {} results", retrieved.size());
        return retrieved;
    }

    private List<RetrievedMemory> semanticSearch(RetrievalQuery query) {
        var queryVec = embeddingProvider.generateEmbedding(query.naturalLanguageQuery());
        var vectorResults = vectorStore.search(queryVec.vector(), query.maxResults() * 2, query.minRelevanceScore());
        return vectorResults.stream()
                .map(vr -> {
                    var memOpt = memoryRepository.findByIdAndIsDeletedFalse(UUID.fromString(vr.id().toString()));
                    return memOpt.map(m -> {
                        var tags = m.getTags().stream().map(t -> t.getName()).toList();
                        return new RetrievedMemory(m.getId(), m.getTitle(), m.getContent(), m.getSummary(),
                                m.getMemoryType(), vr.score(), m.getSource(), m.getPriority(),
                                m.getImportance(), tags, m.getOwnerId(), m.getOwnerType(),
                                m.getWorkspace(), m.getCreatedAt(), m.getLastAccessedAt(),
                                m.getIsEphemeral(), m.getHasEncryption());
                    });
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<RetrievedMemory> keywordSearch(RetrievalQuery query) {
        var keywordMemories = memoryRepository.search(query.naturalLanguageQuery());
        return keywordMemories.stream()
                .map(m -> {
                    double score = computeKeywordScore(m.getContent(), query.naturalLanguageQuery());
                    var tags = m.getTags().stream().map(t -> t.getName()).toList();
                    return new RetrievedMemory(m.getId(), m.getTitle(), m.getContent(), m.getSummary(),
                            m.getMemoryType(), score, m.getSource(), m.getPriority(),
                            m.getImportance(), tags, m.getOwnerId(), m.getOwnerType(),
                            m.getWorkspace(), m.getCreatedAt(), m.getLastAccessedAt(),
                            m.getIsEphemeral(), m.getHasEncryption());
                })
                .collect(Collectors.toList());
    }

    private List<RetrievedMemory> metadataSearch(RetrievalQuery query) {
        var memories = new ArrayList<>(memoryRepository.findByIsDeletedFalse());

        if (query.tags() != null && !query.tags().isEmpty()) {
            memories.retainAll(memoryRepository.findByTagNames(query.tags()));
        }
        if (query.workspaces() != null && !query.workspaces().isEmpty()) {
            memories.removeIf(m -> !query.workspaces().contains(m.getWorkspace()));
        }
        if (query.memoryTypes() != null && !query.memoryTypes().isEmpty()) {
            memories.removeIf(m -> !query.memoryTypes().contains(m.getMemoryType().name()));
        }
        if (query.sources() != null && !query.sources().isEmpty()) {
            memories.removeIf(m -> !query.sources().contains(m.getSource()));
        }
        if (query.ownerId() != null) {
            memories.removeIf(m -> !m.getOwnerId().toString().equals(query.ownerId()));
        }
        if (query.ownerType() != null) {
            memories.removeIf(m -> !m.getOwnerType().equals(query.ownerType()));
        }
        if (query.entityType() != null && query.entityId() != null) {
            memories.removeIf(m -> !(query.entityType().equals(m.getEntityType()) && query.entityId().equals(m.getEntityId().toString())));
        }
        if (query.conversationId() != null) {
            memories.removeIf(m -> m.getConversationId() == null || !m.getConversationId().toString().equals(query.conversationId()));
        }
        if (query.department() != null) {
            memories.removeIf(m -> !query.department().equals(m.getDepartment()));
        }
        if (query.minImportance() != null) {
            memories.removeIf(m -> m.getImportance() < query.minImportance());
        }

        return memories.stream()
                .map(m -> {
                    var tags = m.getTags().stream().map(t -> t.getName()).toList();
                    return new RetrievedMemory(m.getId(), m.getTitle(), m.getContent(), m.getSummary(),
                            m.getMemoryType(), 1.0, m.getSource(), m.getPriority(),
                            m.getImportance(), tags, m.getOwnerId(), m.getOwnerType(),
                            m.getWorkspace(), m.getCreatedAt(), m.getLastAccessedAt(),
                            m.getIsEphemeral(), m.getHasEncryption());
                })
                .collect(Collectors.toList());
    }

    private double computeKeywordScore(String content, String query) {
        if (content == null || query == null) return 0;
        var contentLower = content.toLowerCase();
        var queryTerms = query.toLowerCase().split("\\s+");
        long matches = Arrays.stream(queryTerms).filter(contentLower::contains).count();
        return (double) matches / queryTerms.length;
    }
}
