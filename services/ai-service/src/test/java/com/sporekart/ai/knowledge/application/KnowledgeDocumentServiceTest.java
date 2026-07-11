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
class KnowledgeDocumentServiceTest {

    @Mock private KnowledgeDocumentRepository documentRepository;
    @Mock private KnowledgeCategoryRepository categoryRepository;
    @Mock private KnowledgeDocumentVersionRepository versionRepository;
    @Mock private KnowledgeChunkRepository chunkRepository;
    @Mock private KnowledgeMetadataRepository metadataRepository;
    @Mock private KnowledgeTagRepository tagRepository;
    @Mock private KnowledgeAccessLogRepository accessLogRepository;
    @Mock private KnowledgeRedisCacheService cacheService;
    @Mock private KnowledgeKafkaEventPublisher kafkaPublisher;

    private KnowledgeDocumentService documentService;

    @BeforeEach
    void setUp() {
        documentService = new KnowledgeDocumentService(documentRepository, categoryRepository,
                versionRepository, chunkRepository, metadataRepository, tagRepository,
                accessLogRepository, cacheService, kafkaPublisher, new SimpleMeterRegistry());
    }

    @Test
    void shouldListDocuments() {
        when(documentRepository.findByIsDeletedFalse()).thenReturn(List.of());
        assertTrue(documentService.listDocuments().isEmpty());
    }

    @Test
    void shouldGetDocumentById() {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(id);
        doc.setTitle("test");
        when(cacheService.getCachedDocument(eq(id), any())).thenReturn(doc);
        assertEquals("test", documentService.getDocument(id).getTitle());
    }

    @Test
    void shouldThrowWhenDocumentNotFound() {
        UUID id = UUID.randomUUID();
        when(cacheService.getCachedDocument(eq(id), any())).thenThrow(new KnowledgeNotFoundException("not found"));
        assertThrows(KnowledgeNotFoundException.class, () -> documentService.getDocument(id));
    }

    @Test
    void shouldCreateDocument() {
        KnowledgeCategoryEntity category = new KnowledgeCategoryEntity("Test", null, 0);
        UUID catId = UUID.randomUUID();
        category.setId(catId);
        when(categoryRepository.findByIdAndIsDeletedFalse(catId)).thenReturn(Optional.of(category));
        when(documentRepository.save(any())).thenAnswer(i -> {
            KnowledgeDocumentEntity d = i.getArgument(0);
            d.setId(UUID.randomUUID());
            return d;
        });
        when(versionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        KnowledgeDocumentEntity result = documentService.createDocument(catId, "Test Doc", "Content",
                null, null, null, null, null, null, null, List.of("tag1"), null);
        assertNotNull(result.getId());
        assertEquals("Test Doc", result.getTitle());
        verify(kafkaPublisher).publishDocumentCreated(any(), any(), any());
    }

    @Test
    void shouldRejectCreateWithoutTitle() {
        assertThrows(KnowledgeValidationException.class,
                () -> documentService.createDocument(null, null, "content",
                        null, null, null, null, null, null, null, null, null));
    }

    @Test
    void shouldDeleteDocument() {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(id);
        doc.setTitle("test");
        when(documentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(doc));
        documentService.deleteDocument(id, null);
        assertTrue(doc.isDeleted());
        verify(kafkaPublisher).publishDocumentDeleted(id);
    }

    @Test
    void shouldThrowOnDeleteNotFound() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());
        assertThrows(KnowledgeNotFoundException.class, () -> documentService.deleteDocument(id, null));
    }

    @Test
    void shouldSearchDocuments() {
        when(documentRepository.search("test")).thenReturn(List.of());
        assertTrue(documentService.searchDocuments("test").isEmpty());
    }

    @Test
    void shouldPublishDocument() {
        UUID id = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(id);
        doc.setTitle("test");
        when(documentRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any())).thenReturn(doc);
        KnowledgeDocumentEntity result = documentService.publishDocument(id, null);
        assertEquals("PUBLISHED", result.getStatus());
    }
}
