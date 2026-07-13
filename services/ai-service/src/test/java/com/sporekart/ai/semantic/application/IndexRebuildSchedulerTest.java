package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndexRebuildSchedulerTest {

    @Mock private SemanticIndexService indexService;

    private IndexRebuildScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new IndexRebuildScheduler(indexService);
    }

    @Test
    void testScheduledRebuild() {
        SemanticVectorIndexEntity index = new SemanticVectorIndexEntity();
        index.setId(UUID.randomUUID());
        index.setName("test-index");
        index.setStatus("ACTIVE");

        when(indexService.listIndexes()).thenReturn(List.of(index));

        scheduler.scheduledRebuild();

        verify(indexService).rebuildIndex("test-index");
    }

    @Test
    void testScheduledRebuildWithNoIndexes() {
        when(indexService.listIndexes()).thenReturn(List.of());

        scheduler.scheduledRebuild();

        verify(indexService, never()).rebuildIndex(anyString());
    }

    @Test
    void testScheduledOptimize() {
        SemanticVectorIndexEntity index = new SemanticVectorIndexEntity();
        index.setId(UUID.randomUUID());
        index.setName("test-index");
        index.setStatus("ACTIVE");

        when(indexService.listIndexes()).thenReturn(List.of(index));

        scheduler.scheduledOptimize();

        verify(indexService).optimizeIndex("test-index");
    }

    @Test
    void testTriggerImmediateRebuild() {
        doNothing().when(indexService).rebuildIndex("immediate");

        scheduler.triggerImmediateRebuild("immediate");

        verify(indexService).rebuildIndex("immediate");
        assertEquals(1, scheduler.getTotalRebuildCount());
    }

    @Test
    void testGetTotalRebuildCount() {
        assertEquals(0, scheduler.getTotalRebuildCount());
    }
}
