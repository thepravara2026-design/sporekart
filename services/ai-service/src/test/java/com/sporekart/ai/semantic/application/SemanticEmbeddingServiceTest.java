package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.adapters.ProviderRoutingEmbeddingGenerator;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemanticEmbeddingServiceTest {

    @Mock private SemanticEmbeddingRepository embeddingRepository;
    @Mock private SemanticRedisCacheService cacheService;
    @Mock private SemanticKafkaEventPublisher kafkaPublisher;
    @Mock private ProviderRoutingEmbeddingGenerator embeddingGenerator;
    @Captor private ArgumentCaptor<SemanticEmbeddingEntity> entityCaptor;

    private final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private SemanticEmbeddingService service;

    @BeforeEach
    void setUp() {
        service = new SemanticEmbeddingService(embeddingRepository, cacheService, kafkaPublisher, embeddingGenerator, meterRegistry);
    }

    @Test
    void testCreateEmbedding() {
        UUID createdBy = UUID.randomUUID();
        SemanticEmbeddingEntity saved = new SemanticEmbeddingEntity();
        saved.setId(UUID.randomUUID());
        saved.setContent("test content");
        saved.setProvider("OPENAI");
        saved.setStatus("PENDING");
        when(embeddingRepository.save(any())).thenReturn(saved);

        SemanticEmbeddingEntity result = service.createEmbedding("test content", null, "OPENAI", "text-embedding-3-small", 1536, "PENDING", createdBy);

        assertNotNull(result);
        assertEquals("test content", result.getContent());
        assertEquals("OPENAI", result.getProvider());
        verify(embeddingRepository).save(any());
        verify(kafkaPublisher).publishEmbeddingCreated(eq(result.getId().toString()), eq("OPENAI"), eq(createdBy));
        verify(cacheService).invalidateEmbeddingMetadata(any());
    }

    @Test
    void testGetEmbeddingFromCache() {
        UUID id = UUID.randomUUID();
        SemanticEmbeddingEntity cached = new SemanticEmbeddingEntity();
        cached.setId(id);
        cached.setContent("cached content");
        when(cacheService.getCachedEmbeddingMetadata(eq(id), any())).thenReturn(cached);

        SemanticEmbeddingEntity result = service.getEmbedding(id);

        assertNotNull(result);
        assertEquals("cached content", result.getContent());
        verify(embeddingRepository, never()).findByIdAndIsDeletedFalse(any());
    }

    @Test
    void testGetEmbeddingFromRepo() {
        UUID id = UUID.randomUUID();
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setId(id);
        entity.setContent("repo content");
        when(cacheService.getCachedEmbeddingMetadata(eq(id), any())).thenAnswer(invocation -> {
            var supplier = invocation.getArgument(1, java.util.function.Supplier.class);
            return supplier.get();
        });
        when(embeddingRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));

        SemanticEmbeddingEntity result = service.getEmbedding(id);

        assertNotNull(result);
        assertEquals("repo content", result.getContent());
        verify(embeddingRepository).findByIdAndIsDeletedFalse(id);
    }

    @Test
    void testGetEmbeddingNotFound() {
        UUID id = UUID.randomUUID();
        when(cacheService.getCachedEmbeddingMetadata(eq(id), any())).thenAnswer(invocation -> {
            var supplier = invocation.getArgument(1, java.util.function.Supplier.class);
            return supplier.get();
        });
        when(embeddingRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        assertThrows(EmbeddingException.class, () -> service.getEmbedding(id));
    }

    @Test
    void testListEmbeddings() {
        when(embeddingRepository.findByIsDeletedFalse()).thenReturn(List.of(new SemanticEmbeddingEntity()));
        assertEquals(1, service.listEmbeddings().size());
    }

    @Test
    void testDeleteEmbedding() {
        UUID id = UUID.randomUUID();
        UUID deletedBy = UUID.randomUUID();
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setId(id);
        when(embeddingRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.of(entity));
        when(embeddingRepository.save(any())).thenReturn(entity);

        service.deleteEmbedding(id, deletedBy);

        assertTrue(entity.isDeleted());
        verify(kafkaPublisher).publishEmbeddingDeleted(id.toString());
        verify(cacheService).invalidateEmbeddingMetadata(id);
    }

    @Test
    void testCreateEmbeddingThrowsOnBlankContent() {
        assertThrows(EmbeddingException.class, () -> service.createEmbedding("", null, "OPENAI", null, 0, null, null));
        assertThrows(EmbeddingException.class, () -> service.createEmbedding(null, null, "OPENAI", null, 0, null, null));
    }

    @Test
    void testGenerateAndStoreEmbedding() {
        UUID createdBy = UUID.randomUUID();
        List<Double> mockVector = List.of(0.1, 0.2, 0.3);
        when(embeddingGenerator.generate(anyString(), anyString(), anyString())).thenReturn(mockVector);
        when(embeddingRepository.save(any())).thenAnswer(invocation -> {
            SemanticEmbeddingEntity e = invocation.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        SemanticEmbeddingEntity result = service.generateAndStoreEmbedding("test", "OPENAI", "text-embedding-3-small", createdBy);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(3, result.getDimensions());
        assertNotNull(result.getEmbedding());
        verify(embeddingGenerator).generate(anyString(), anyString(), anyString());
    }
}
