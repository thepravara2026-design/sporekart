package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.EmbeddingStatus;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class EmbeddingVersionManagerTest {

    @Mock private SemanticEmbeddingRepository embeddingRepository;

    private EmbeddingVersionManager versionManager;

    @BeforeEach
    void setUp() {
        versionManager = new EmbeddingVersionManager(embeddingRepository);
    }

    private SemanticEmbeddingEntity createEntity(int version, String status) {
        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setId(UUID.randomUUID());
        entity.setContent("test content");
        entity.setEmbedding("[0.1,0.2]");
        entity.setProvider("OPENAI");
        entity.setModel("text-embedding-3-small");
        entity.setDimensions(1536);
        entity.setStatus(status);
        entity.setVersion(version);
        entity.setCreatedAt(OffsetDateTime.now());
        return entity;
    }

    @Test
    void testCreateNewVersion() {
        UUID existingId = UUID.randomUUID();
        SemanticEmbeddingEntity existing = createEntity(1, "COMPLETED");
        existing.setId(existingId);
        when(embeddingRepository.findByIdAndIsDeletedFalse(existingId)).thenReturn(Optional.of(existing));
        when(embeddingRepository.save(any())).thenAnswer(invocation -> {
            SemanticEmbeddingEntity e = invocation.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        SemanticEmbeddingEntity newVersion = versionManager.createNewVersion(existingId, "new content", null, null, null, 0, UUID.randomUUID());

        assertNotNull(newVersion);
        assertEquals(2, newVersion.getVersion());
        assertEquals(EmbeddingStatus.PENDING.name(), newVersion.getStatus());
    }

    @Test
    void testCreateNewVersionThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(embeddingRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());
        assertThrows(EmbeddingException.class, () -> versionManager.createNewVersion(id, "content", null, null, null, 0, UUID.randomUUID()));
    }

    @Test
    void testDeprecateVersion() {
        SemanticEmbeddingEntity entity = createEntity(1, "COMPLETED");
        when(embeddingRepository.findByIdAndIsDeletedFalse(entity.getId())).thenReturn(Optional.of(entity));

        versionManager.deprecateVersion(entity.getId());

        assertEquals(EmbeddingStatus.DEPRECATED.name(), entity.getStatus());
    }

    @Test
    void testFindDeprecatedEmbeddings() {
        when(embeddingRepository.findByStatusAndIsDeletedFalse(EmbeddingStatus.DEPRECATED.name())).thenReturn(List.of());
        assertTrue(versionManager.findDeprecatedEmbeddings().isEmpty());
    }

    @Test
    void testFindActiveEmbeddings() {
        SemanticEmbeddingEntity active = createEntity(1, "COMPLETED");
        when(embeddingRepository.findByStatusAndIsDeletedFalse(EmbeddingStatus.COMPLETED.name())).thenReturn(List.of(active));
        assertEquals(1, versionManager.findActiveEmbeddings().size());
    }

    @Test
    void testUpgradeEmbedding() {
        UUID existingId = UUID.randomUUID();
        SemanticEmbeddingEntity existing = createEntity(1, "COMPLETED");
        existing.setId(existingId);
        when(embeddingRepository.findByIdAndIsDeletedFalse(existingId)).thenReturn(Optional.of(existing));
        when(embeddingRepository.save(any())).thenAnswer(invocation -> {
            SemanticEmbeddingEntity e = invocation.getArgument(0);
            if (e.getId() == null) e.setId(UUID.randomUUID());
            return e;
        });

        versionManager.upgradeEmbedding(existingId, "[0.5,0.6]", "new-model", 512, UUID.randomUUID());

        assertEquals(EmbeddingStatus.DEPRECATED.name(), existing.getStatus());
        verify(embeddingRepository, times(2)).save(any());
    }
}
