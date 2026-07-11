package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeMetadataServiceTest {

    @Mock private KnowledgeMetadataRepository metadataRepository;
    @Mock private KnowledgeTagRepository tagRepository;
    @Mock private KnowledgeCategoryRepository categoryRepository;
    @Mock private KnowledgeRedisCacheService cacheService;

    private KnowledgeMetadataService metadataService;

    @BeforeEach
    void setUp() {
        metadataService = new KnowledgeMetadataService(metadataRepository, tagRepository, categoryRepository, cacheService);
    }

    @Test
    void shouldReturnEmptyMetadataWhenCached() {
        UUID docId = UUID.randomUUID();
        when(cacheService.getCachedMetadata(docId)).thenReturn(Optional.of("some-cached"));
        assertTrue(metadataService.getDocumentMetadata(docId).isEmpty());
    }

    @Test
    void shouldReturnMetadataFromRepository() {
        UUID docId = UUID.randomUUID();
        when(cacheService.getCachedMetadata(docId)).thenReturn(Optional.empty());
        when(metadataRepository.findByDocumentId(docId)).thenReturn(List.of());
        assertTrue(metadataService.getDocumentMetadata(docId).isEmpty());
    }

    @Test
    void shouldSetMetadata() {
        UUID docId = UUID.randomUUID();
        when(metadataRepository.findByDocumentId(docId)).thenReturn(List.of());
        metadataService.setMetadata(docId, "key1", "value1");
        verify(metadataRepository).save(any());
    }

    @Test
    void shouldUpdateExistingMetadata() {
        UUID docId = UUID.randomUUID();
        KnowledgeMetadataEntity existing = new KnowledgeMetadataEntity();
        existing.setMetaKey("key1");
        existing.setMetaValue("old");
        when(metadataRepository.findByDocumentId(docId)).thenReturn(List.of(existing));
        metadataService.setMetadata(docId, "key1", "new");
        assertEquals("new", existing.getMetaValue());
    }

    @Test
    void shouldListActiveCategories() {
        KnowledgeCategoryEntity cat = new KnowledgeCategoryEntity("FAQ", null, 0);
        cat.setId(UUID.randomUUID());
        when(cacheService.getCachedCategoryList()).thenReturn(Optional.empty());
        when(categoryRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrder()).thenReturn(List.of(cat));
        List<KnowledgeCategoryEntity> cats = metadataService.listCategories();
        assertFalse(cats.isEmpty());
        assertEquals("FAQ", cats.get(0).getName());
    }

    @Test
    void shouldGetDocumentTags() {
        UUID docId = UUID.randomUUID();
        KnowledgeTagEntity tag = new KnowledgeTagEntity();
        tag.setTag("test-tag");
        when(tagRepository.findByDocumentId(docId)).thenReturn(List.of(tag));
        List<String> tags = metadataService.getDocumentTags(docId);
        assertEquals(1, tags.size());
        assertEquals("test-tag", tags.get(0));
    }
}
