package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeRetrievalServiceTest {

    @Mock private KnowledgeDocumentRepository documentRepository;
    @Mock private KnowledgeChunkRepository chunkRepository;
    @Mock private KnowledgeCategoryRepository categoryRepository;
    @Mock private KnowledgeCitationRepository citationRepository;
    @Mock private KnowledgeAccessLogRepository accessLogRepository;
    @Mock private KnowledgeRedisCacheService cacheService;
    @Mock private KnowledgeKafkaEventPublisher kafkaPublisher;

    private KnowledgeRetrievalService retrievalService;

    @BeforeEach
    void setUp() {
        retrievalService = new KnowledgeRetrievalService(documentRepository, chunkRepository,
                categoryRepository, citationRepository, accessLogRepository, cacheService, kafkaPublisher,
                new SimpleMeterRegistry());
    }

    @Test
    void shouldRetrieveDocuments() {
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(UUID.randomUUID());
        doc.setTitle("Test Doc");
        doc.setContent("Some content about knowledge retrieval");
        doc.setActive(true);
        doc.setVisibility("INTERNAL");
        when(documentRepository.findByVisibilityAndIsActiveTrue("INTERNAL")).thenReturn(List.of(doc));
        when(chunkRepository.findByDocumentIdAndIsActiveTrueAndIsDeletedFalseOrderByChunkIndex(any()))
                .thenReturn(List.of());
        when(citationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        KnowledgeRetrievalService.RetrievalResult result = retrievalService.retrieve("knowledge",
                null, null, null, null, 5);
        assertNotNull(result.requestId());
        assertFalse(result.documents().isEmpty());
        assertEquals("Test Doc", result.documents().get(0).getTitle());
        verify(kafkaPublisher).publishRetrieved(any(), any());
    }

    @Test
    void shouldRetrieveByCategory() {
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(UUID.randomUUID());
        doc.setTitle("Test Doc");
        doc.setContent("Content");
        doc.setActive(true);

        KnowledgeCategoryEntity cat = new KnowledgeCategoryEntity();
        cat.setId(UUID.randomUUID());
        cat.setName("FAQ");
        when(categoryRepository.findByNameAndIsDeletedFalse("FAQ")).thenReturn(Optional.of(cat));
        when(documentRepository.findByIsDeletedFalse()).thenReturn(List.of(doc));

        KnowledgeRetrievalService.RetrievalResult result = retrievalService.retrieve(null,
                List.of("FAQ"), null, null, null, 5);
        assertNotNull(result.requestId());
    }

    @Test
    void shouldGetCitations() {
        UUID requestId = UUID.randomUUID();
        when(citationRepository.findByRetrievalRequestId(requestId)).thenReturn(List.of());
        assertTrue(retrievalService.getCitations(requestId).isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenNoMatchingDocuments() {
        when(documentRepository.findByVisibilityAndIsActiveTrue("INTERNAL")).thenReturn(List.of());
        KnowledgeRetrievalService.RetrievalResult result = retrievalService.retrieve(null, null,
                null, null, null, 5);
        assertTrue(result.documents().isEmpty());
        assertTrue(result.chunks().isEmpty());
    }
}
