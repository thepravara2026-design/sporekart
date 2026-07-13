package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.VectorIndexManager;
import com.sporekart.ai.semantic.domain.IndexStatus;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticIndexStatisticsRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VectorIndexManagerTest {

    @Mock private SemanticVectorIndexRepository indexRepository;
    @Mock private SemanticIndexStatisticsRepository statisticsRepository;
    @Mock private SemanticRedisCacheService cacheService;
    @Mock private SemanticKafkaEventPublisher kafkaPublisher;

    private SemanticIndexService indexService;

    @BeforeEach
    void setUp() {
        indexService = new SemanticIndexService(indexRepository, statisticsRepository, cacheService, kafkaPublisher);
    }

    @Test
    void testCreateIndex() {
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.empty());
        when(indexRepository.save(any())).thenAnswer(invocation -> {
            SemanticVectorIndexEntity e = invocation.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        SemanticVectorIndexEntity result = indexService.createIndex("test", "desc", 768, null);

        assertNotNull(result);
        assertEquals("test", result.getName());
        assertEquals(IndexStatus.CREATING.name(), result.getStatus());
        assertEquals(768, result.getDimensions());
    }

    @Test
    void testRebuildIndex() {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test");
        entity.setStatus(IndexStatus.ACTIVE.name());
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        indexService.rebuildIndex("test");

        assertEquals(IndexStatus.REBUILDING.name(), entity.getStatus());
        verify(kafkaPublisher).publishVectorIndexRebuilt(entity.getId().toString());
    }

    @Test
    void testOptimizeIndex() {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test");
        entity.setStatus(IndexStatus.ACTIVE.name());
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        indexService.optimizeIndex("test");

        assertEquals(IndexStatus.OPTIMIZING.name(), entity.getStatus());
    }

    @Test
    void testGetIndexStatus() {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test");
        entity.setStatus(IndexStatus.ACTIVE.name());
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        assertEquals(IndexStatus.ACTIVE, indexService.getIndexStatus("test"));
    }

    @Test
    void testDeleteIndex() {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("test");
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        indexService.deleteIndex("test");

        assertTrue(entity.isDeleted());
        verify(cacheService).invalidateIndexMetadata("test");
    }
}
