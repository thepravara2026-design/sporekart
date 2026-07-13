package com.sporekart.ai.knowledgeregistry.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceEntry;
import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.SyncStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceEntity;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class KnowledgeRegistryServiceTest {

    @Mock
    private KnowledgeSourceRepository sourceRepository;

    @InjectMocks
    private KnowledgeRegistryServiceImpl service;

    @Test
    void registerSourcePersistsEntity() {
        KnowledgeSourceEntry entry = new KnowledgeSourceEntry();
        entry.setSourceName("Docs");
        entry.setSourceType(KnowledgeSourceType.DATABASE);

        when(sourceRepository.save(any(KnowledgeSourceEntity.class))).thenAnswer(i -> i.getArgument(0));

        KnowledgeSourceEntry saved = service.registerSource(entry);

        assertNotNull(saved);
        assertEquals("Docs", saved.getSourceName());
    }

    @Test
    void getByTypeReturnsMatchingSources() {
        KnowledgeSourceEntity entity = new KnowledgeSourceEntity();
        entity.setSourceId("s1");
        entity.setSourceName("Docs");
        entity.setSourceType(KnowledgeSourceType.DATABASE);
        when(sourceRepository.findBySourceType(KnowledgeSourceType.DATABASE)).thenReturn(List.of(entity));

        List<KnowledgeSourceEntry> result = service.getByType(KnowledgeSourceType.DATABASE);

        assertEquals(1, result.size());
        assertEquals(KnowledgeSourceType.DATABASE, result.get(0).getSourceType());
    }

    @Test
    void triggerSyncTransitionsStatusToInProgress() {
        KnowledgeSourceEntity entity = new KnowledgeSourceEntity();
        entity.setSourceId("s1");
        entity.setSourceName("Docs");
        entity.setSourceType(KnowledgeSourceType.DATABASE);
        entity.setSyncStatus(SyncStatus.PENDING);
        when(sourceRepository.findById("s1")).thenReturn(Optional.of(entity));
        when(sourceRepository.save(any(KnowledgeSourceEntity.class))).thenAnswer(i -> i.getArgument(0));

        KnowledgeSourceEntry result = service.triggerSync("s1");

        assertEquals(SyncStatus.IN_PROGRESS, result.getSyncStatus());
    }
}
