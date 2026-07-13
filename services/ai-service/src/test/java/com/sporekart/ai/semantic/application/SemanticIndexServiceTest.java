package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.IndexStatus;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticIndexStatisticsEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticIndexStatisticsRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SemanticIndexServiceTest {

    @Mock private SemanticVectorIndexRepository indexRepository;
    @Mock private SemanticIndexStatisticsRepository statisticsRepository;
    @Mock private SemanticRedisCacheService cacheService;
    @Mock private SemanticKafkaEventPublisher kafkaPublisher;

    private SemanticIndexService service;

    @BeforeEach
    void setUp() {
        service = new SemanticIndexService(indexRepository, statisticsRepository, cacheService, kafkaPublisher);
    }

    private SemanticVectorIndexEntity createIndexEntity(String name, String status, int dimensions) {
        SemanticVectorIndexEntity entity = new SemanticVectorIndexEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(name);
        entity.setStatus(status);
        entity.setDimensions(dimensions);
        entity.setVectorCount(0);
        return entity;
    }

    @Test
    void testCreateIndex() {
        when(indexRepository.findByNameAndIsDeletedFalse("test-index")).thenReturn(Optional.empty());
        when(indexRepository.save(any())).thenAnswer(invocation -> {
            SemanticVectorIndexEntity e = invocation.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        SemanticVectorIndexEntity result = service.createIndex("test-index", "Test description", 768, null);

        assertNotNull(result);
        assertEquals("test-index", result.getName());
        assertEquals(IndexStatus.CREATING.name(), result.getStatus());
        assertEquals(768, result.getDimensions());
        verify(kafkaPublisher).publishVectorIndexBuilt(result.getId().toString());
    }

    @Test
    void testCreateIndexThrowsOnDuplicate() {
        when(indexRepository.findByNameAndIsDeletedFalse("existing")).thenReturn(Optional.of(new SemanticVectorIndexEntity()));
        assertThrows(IndexException.class, () -> service.createIndex("existing", "desc", 768, null));
    }

    @Test
    void testRebuildIndex() {
        SemanticVectorIndexEntity entity = createIndexEntity("test", "ACTIVE", 768);
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        service.rebuildIndex("test");

        assertEquals(IndexStatus.REBUILDING.name(), entity.getStatus());
        verify(kafkaPublisher).publishVectorIndexRebuilt(entity.getId().toString());
        verify(cacheService).invalidateIndexMetadata("test");
    }

    @Test
    void testOptimizeIndex() {
        SemanticVectorIndexEntity entity = createIndexEntity("test", "ACTIVE", 768);
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        service.optimizeIndex("test");

        assertEquals(IndexStatus.OPTIMIZING.name(), entity.getStatus());
        verify(cacheService).invalidateIndexMetadata("test");
    }

    @Test
    void testActivateIndex() {
        SemanticVectorIndexEntity entity = createIndexEntity("test", "CREATING", 768);
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        service.activateIndex("test");

        assertEquals(IndexStatus.ACTIVE.name(), entity.getStatus());
    }

    @Test
    void testGetIndexStatus() {
        SemanticVectorIndexEntity entity = createIndexEntity("test", "ACTIVE", 768);
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        assertEquals(IndexStatus.ACTIVE, service.getIndexStatus("test"));
    }

    @Test
    void testDeleteIndex() {
        SemanticVectorIndexEntity entity = createIndexEntity("test", "ACTIVE", 768);
        when(indexRepository.findByNameAndIsDeletedFalse("test")).thenReturn(Optional.of(entity));

        service.deleteIndex("test");

        assertTrue(entity.isDeleted());
        verify(cacheService).invalidateIndexMetadata("test");
    }

    @Test
    void testGetIndexStatistics() {
        String indexName = "test";
        when(cacheService.getCachedStatistics(indexName)).thenReturn(null);
        SemanticIndexStatisticsEntity stat = new SemanticIndexStatisticsEntity();
        stat.setStatKey("vector_count");
        stat.setStatValue(100.0);
        when(statisticsRepository.findByIndexNameAndIsDeletedFalse(indexName)).thenReturn(List.of(stat));

        Map<String, Double> stats = service.getIndexStatistics(indexName);
        assertEquals(100.0, stats.get("vector_count"));
    }

    @Test
    void testListIndexes() {
        when(indexRepository.findByIsDeletedFalse()).thenReturn(List.of(createIndexEntity("a", "ACTIVE", 768), createIndexEntity("b", "ACTIVE", 1024)));
        assertEquals(2, service.listIndexes().size());
    }
}
