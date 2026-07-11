package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeChunkEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentEntity;
import com.sporekart.ai.knowledge.infrastructure.persistence.KnowledgeDocumentRepository;
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
class KnowledgeChunkingServiceTest {

    @Mock private KnowledgeChunkRepository chunkRepository;
    @Mock private KnowledgeDocumentRepository documentRepository;
    @Mock private KnowledgeKafkaEventPublisher kafkaPublisher;

    private KnowledgeChunkingService chunkingService;

    @BeforeEach
    void setUp() {
        chunkingService = new KnowledgeChunkingService(chunkRepository, documentRepository, kafkaPublisher);
    }

    @Test
    void shouldGetChunks() {
        UUID docId = UUID.randomUUID();
        when(chunkRepository.findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(docId)).thenReturn(List.of());
        assertTrue(chunkingService.getChunks(docId).isEmpty());
    }

    @Test
    void shouldChunkDocument() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setTitle("test");
        doc.setContent("Hello world. This is a test document with multiple sentences. " +
                "It should be chunked into smaller pieces. " +
                "Each chunk should have meaningful content.");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        when(chunkRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<KnowledgeChunkEntity> chunks = chunkingService.chunkDocument(docId, 50, 10);
        assertFalse(chunks.isEmpty());
        verify(kafkaPublisher).publishIndexed(eq(docId), eq(chunks.size()));
    }

    @Test
    void shouldThrowWhenDocumentNotFound() {
        UUID docId = UUID.randomUUID();
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.empty());
        assertThrows(KnowledgeNotFoundException.class, () -> chunkingService.chunkDocument(docId, 100, 10));
    }

    @Test
    void shouldThrowWhenContentEmpty() {
        UUID docId = UUID.randomUUID();
        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
        doc.setId(docId);
        doc.setContent("");
        when(documentRepository.findByIdAndIsDeletedFalse(docId)).thenReturn(Optional.of(doc));
        assertThrows(KnowledgeValidationException.class, () -> chunkingService.chunkDocument(docId, 100, 10));
    }

    @Test
    void shouldValidateChunks() {
        UUID docId = UUID.randomUUID();
        KnowledgeChunkEntity chunk = new KnowledgeChunkEntity();
        chunk.setContent("valid content");
        chunk.setChunkIndex(0);
        chunk.setCharCount(13);
        when(chunkRepository.findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(docId))
                .thenReturn(List.of(chunk));
        assertDoesNotThrow(() -> chunkingService.validateChunks(docId));
    }

    @Test
    void shouldThrowOnEmptyChunksValidation() {
        UUID docId = UUID.randomUUID();
        when(chunkRepository.findByDocumentIdAndIsDeletedFalseOrderByChunkIndex(docId))
                .thenReturn(List.of());
        assertThrows(KnowledgeValidationException.class, () -> chunkingService.validateChunks(docId));
    }
}
