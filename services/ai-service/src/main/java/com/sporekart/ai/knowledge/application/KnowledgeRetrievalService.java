package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class KnowledgeRetrievalService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeRetrievalService.class);

    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeCategoryRepository categoryRepository;
    private final KnowledgeCitationRepository citationRepository;
    private final KnowledgeAccessLogRepository accessLogRepository;
    private final KnowledgeRedisCacheService cacheService;
    private final KnowledgeKafkaEventPublisher kafkaPublisher;
    private final Counter retrievalCounter;
    private final Counter searchCounter;
    private final Timer retrievalTimer;

    public KnowledgeRetrievalService(KnowledgeDocumentRepository documentRepository,
                                     KnowledgeChunkRepository chunkRepository,
                                     KnowledgeCategoryRepository categoryRepository,
                                     KnowledgeCitationRepository citationRepository,
                                     KnowledgeAccessLogRepository accessLogRepository,
                                     KnowledgeRedisCacheService cacheService,
                                     KnowledgeKafkaEventPublisher kafkaPublisher,
                                     MeterRegistry meterRegistry) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
        this.categoryRepository = categoryRepository;
        this.citationRepository = citationRepository;
        this.accessLogRepository = accessLogRepository;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
        this.retrievalCounter = Counter.builder("knowledge.retrieval.count")
                .description("Number of knowledge retrieval requests").register(meterRegistry);
        this.searchCounter = Counter.builder("knowledge.search.count")
                .description("Number of knowledge search requests").register(meterRegistry);
        this.retrievalTimer = Timer.builder("knowledge.retrieval.duration")
                .description("Knowledge retrieval duration").register(meterRegistry);
    }

    public RetrievalResult retrieve(String query, List<String> categories, String language,
                                     String visibility, String businessModule, int maxChunks) {
        long start = System.nanoTime();
        retrievalCounter.increment();
        UUID requestId = UUID.randomUUID();
        List<KnowledgeDocumentEntity> documents;

        if (categories != null && !categories.isEmpty()) {
            List<UUID> categoryIds = new ArrayList<>();
            for (String catName : categories) {
                categoryRepository.findByNameAndIsDeletedFalse(catName)
                        .ifPresent(c -> categoryIds.add(c.getId()));
            }
            documents = documentRepository.findByIsDeletedFalse().stream()
                    .filter(d -> d.isActive() && !d.isDeleted())
                    .filter(d -> categoryIds.contains(d.getCategory() != null ? d.getCategory().getId() : null))
                    .collect(Collectors.toList());
        } else {
            documents = documentRepository.findByVisibilityAndIsActiveTrue(visibility != null ? visibility : "INTERNAL").stream()
                    .filter(d -> !d.isDeleted())
                    .collect(Collectors.toList());
        }

        if (language != null) {
            documents = documents.stream()
                    .filter(d -> language.equals(d.getLanguage()))
                    .collect(Collectors.toList());
        }

        if (businessModule != null) {
            documents = documents.stream()
                    .filter(d -> businessModule.equals(d.getBusinessModule()))
                    .collect(Collectors.toList());
        }

        if (query != null && !query.isBlank()) {
            String lowerQuery = query.toLowerCase();
            documents = documents.stream()
                    .filter(d -> (d.getTitle() != null && d.getTitle().toLowerCase().contains(lowerQuery))
                            || (d.getContent() != null && d.getContent().toLowerCase().contains(lowerQuery))
                            || (d.getDescription() != null && d.getDescription().toLowerCase().contains(lowerQuery)))
                    .collect(Collectors.toList());
        }

        int limit = maxChunks > 0 ? Math.min(maxChunks, 20) : 10;
        List<KnowledgeChunkEntity> selectedChunks = new ArrayList<>();
        List<KnowledgeCitationEntity> citations = new ArrayList<>();

        for (KnowledgeDocumentEntity doc : documents.subList(0, Math.min(documents.size(), limit))) {
            List<KnowledgeChunkEntity> chunks = chunkRepository
                    .findByDocumentIdAndIsActiveTrueAndIsDeletedFalseOrderByChunkIndex(doc.getId());
            selectedChunks.addAll(chunks.stream().limit(3).collect(Collectors.toList()));

            KnowledgeCitationEntity citation = new KnowledgeCitationEntity(doc);
            citation.setRetrievalRequestId(requestId);
            citation.setChunkIds(chunks.stream().map(c -> c.getId().toString()).toArray(String[]::new));
            citation.setExcerpts(chunks.stream().map(c -> c.getContent().substring(0, Math.min(c.getContent().length(), 200))).toArray(String[]::new));
            citation.setRelevanceScore(1.0);
            citation.setRetrievalContext("keyword-match");
            citation = citationRepository.save(citation);
            citations.add(citation);

            accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_RETRIEVED"));
            kafkaPublisher.publishRetrieved(doc.getId(), requestId);
        }

        long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        retrievalTimer.record(durationMs, TimeUnit.MILLISECONDS);
        log.info("Retrieval {} completed in {}ms with {} docs, {} chunks", requestId, durationMs,
                documents.size(), selectedChunks.size());
        return new RetrievalResult(requestId, documents, selectedChunks, citations);
    }

    public List<KnowledgeCitationEntity> getCitations(UUID requestId) {
        return citationRepository.findByRetrievalRequestId(requestId);
    }

    public record RetrievalResult(
            UUID requestId,
            List<KnowledgeDocumentEntity> documents,
            List<KnowledgeChunkEntity> chunks,
            List<KnowledgeCitationEntity> citations) {}
}
