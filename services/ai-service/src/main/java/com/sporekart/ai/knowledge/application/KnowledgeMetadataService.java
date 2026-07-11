package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KnowledgeMetadataService {

    private final KnowledgeMetadataRepository metadataRepository;
    private final KnowledgeTagRepository tagRepository;
    private final KnowledgeCategoryRepository categoryRepository;
    private final KnowledgeRedisCacheService cacheService;

    public KnowledgeMetadataService(KnowledgeMetadataRepository metadataRepository,
                                    KnowledgeTagRepository tagRepository,
                                    KnowledgeCategoryRepository categoryRepository,
                                    KnowledgeRedisCacheService cacheService) {
        this.metadataRepository = metadataRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.cacheService = cacheService;
    }

    public Map<String, String> getDocumentMetadata(UUID documentId) {
        Optional<String> cached = cacheService.getCachedMetadata(documentId);
        if (cached.isPresent()) return Map.of();

        List<KnowledgeMetadataEntity> entities = metadataRepository.findByDocumentId(documentId);
        Map<String, String> result = new LinkedHashMap<>();
        for (KnowledgeMetadataEntity entity : entities) {
            result.put(entity.getMetaKey(), entity.getMetaValue());
        }
        return result;
    }

    @Transactional
    public void setMetadata(UUID documentId, String key, String value) {
        List<KnowledgeMetadataEntity> existing = metadataRepository.findByDocumentId(documentId);
        Optional<KnowledgeMetadataEntity> match = existing.stream()
                .filter(m -> m.getMetaKey().equals(key))
                .findFirst();
        if (match.isPresent()) {
            KnowledgeMetadataEntity entity = match.get();
            entity.setMetaValue(value);
            entity.setUpdatedAt(OffsetDateTime.now());
            metadataRepository.save(entity);
        } else {
            KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity();
            doc.setId(documentId);
            metadataRepository.save(new KnowledgeMetadataEntity(doc, key, value));
        }
        cacheService.cacheMetadata(documentId, key + "=" + value);
    }

    @Transactional
    public void deleteMetadata(UUID documentId, String key) {
        List<KnowledgeMetadataEntity> existing = metadataRepository.findByDocumentId(documentId);
        existing.stream().filter(m -> m.getMetaKey().equals(key)).forEach(metadataRepository::delete);
        cacheService.invalidateDocument(documentId);
    }

    public List<KnowledgeCategoryEntity> listCategories() {
        Optional<String> cached = cacheService.getCachedCategoryList();
        if (cached.isPresent()) return categoryRepository.findByIsDeletedFalseOrderByDisplayOrder();
        List<KnowledgeCategoryEntity> categories = categoryRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrder();
        return categories;
    }

    public List<String> getDocumentTags(UUID documentId) {
        return tagRepository.findByDocumentId(documentId).stream()
                .map(KnowledgeTagEntity::getTag)
                .collect(Collectors.toList());
    }

    public List<KnowledgeDocumentEntity> findDocumentsByTag(String tag) {
        return tagRepository.findByTag(tag.toLowerCase()).stream()
                .map(KnowledgeTagEntity::getDocument)
                .filter(d -> !d.isDeleted())
                .collect(Collectors.toList());
    }
}
