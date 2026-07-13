package com.sporekart.ai.promptregistry.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sporekart.ai.promptregistry.domain.PromptComparisonResult;
import com.sporekart.ai.promptregistry.domain.PromptRegistryEntry;
import com.sporekart.ai.promptregistry.domain.PromptStatus;
import com.sporekart.ai.promptregistry.domain.PromptVersionInfo;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryEntity;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptRegistryRepository;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptVersionHistoryEntity;
import com.sporekart.ai.promptregistry.infrastructure.persistence.PromptVersionHistoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PromptRegistryServiceTest {

    @Mock
    private PromptRegistryRepository registryRepository;

    @Mock
    private PromptVersionHistoryRepository historyRepository;

    @InjectMocks
    private PromptRegistryServiceImpl service;

    @Test
    void registerPromptPersistsEntry() {
        PromptRegistryEntry entry = new PromptRegistryEntry();
        entry.setPromptName("Greeting");
        entry.setPromptText("Hello");
        entry.setStatus(PromptStatus.DRAFT);

        when(registryRepository.save(any(PromptRegistryEntity.class))).thenAnswer(i -> i.getArgument(0));

        PromptRegistryEntry saved = service.registerPrompt(entry);

        assertNotNull(saved);
        assertEquals("Greeting", saved.getPromptName());
    }

    @Test
    void getVersionsReturnsHistory() {
        PromptVersionHistoryEntity history = new PromptVersionHistoryEntity();
        history.setVersion(1);
        history.setStatus(PromptStatus.DRAFT);
        history.setChangeSummary("Initial registration");
        history.setCreatedAt(Instant.now());
        when(historyRepository.findByPromptIdOrderByVersionDesc("p1")).thenReturn(List.of(history));

        List<PromptVersionInfo> versions = service.getVersions("p1");

        assertEquals(1, versions.size());
        assertEquals(1, versions.get(0).version());
    }

    @Test
    void rollbackToVersionCreatesNewVersion() {
        PromptRegistryEntity entity = new PromptRegistryEntity();
        entity.setPromptId("p1");
        entity.setPromptText("old-text");
        entity.setStatus(PromptStatus.DRAFT);
        entity.setVersion(1);

        PromptVersionHistoryEntity target = new PromptVersionHistoryEntity();
        target.setVersion(1);
        target.setPromptText("old-text");
        target.setStatus(PromptStatus.DRAFT);

        when(registryRepository.findByPromptId("p1")).thenReturn(Optional.of(entity));
        when(historyRepository.findByPromptIdAndVersion("p1", 1)).thenReturn(Optional.of(target));
        when(registryRepository.save(any(PromptRegistryEntity.class))).thenAnswer(i -> i.getArgument(0));

        PromptRegistryEntry rolledBack = service.rollbackToVersion("p1", 1);

        assertEquals(2, rolledBack.getVersion());
        assertEquals("old-text", rolledBack.getPromptText());
    }

    @Test
    void compareVersionsDetectsDifferences() {
        PromptVersionHistoryEntity a = new PromptVersionHistoryEntity();
        a.setPromptText("version-a");
        PromptVersionHistoryEntity b = new PromptVersionHistoryEntity();
        b.setPromptText("version-b");

        when(historyRepository.findByPromptIdAndVersion("p1", 1)).thenReturn(Optional.of(a));
        when(historyRepository.findByPromptIdAndVersion("p1", 2)).thenReturn(Optional.of(b));

        PromptComparisonResult result = service.compareVersions("p1", 1, 2);

        assertNotNull(result);
        assertFalse(result.differences().isEmpty());
    }
}
