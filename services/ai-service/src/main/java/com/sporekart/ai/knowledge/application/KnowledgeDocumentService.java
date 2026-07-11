package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.infrastructure.KnowledgeKafkaEventPublisher;
import com.sporekart.ai.knowledge.infrastructure.KnowledgeRedisCacheService;
import com.sporekart.ai.knowledge.infrastructure.persistence.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KnowledgeDocumentService {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeDocumentService.class);

    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeCategoryRepository categoryRepository;
    private final KnowledgeDocumentVersionRepository versionRepository;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeMetadataRepository metadataRepository;
    private final KnowledgeTagRepository tagRepository;
    private final KnowledgeAccessLogRepository accessLogRepository;
    private final KnowledgeRedisCacheService cacheService;
    private final KnowledgeKafkaEventPublisher kafkaPublisher;
    private final Counter documentCreateCounter;
    private final Counter documentReadCounter;
    private final Counter documentDeleteCounter;

    public KnowledgeDocumentService(KnowledgeDocumentRepository documentRepository,
                                    KnowledgeCategoryRepository categoryRepository,
                                    KnowledgeDocumentVersionRepository versionRepository,
                                    KnowledgeChunkRepository chunkRepository,
                                    KnowledgeMetadataRepository metadataRepository,
                                    KnowledgeTagRepository tagRepository,
                                    KnowledgeAccessLogRepository accessLogRepository,
                                    KnowledgeRedisCacheService cacheService,
                                    KnowledgeKafkaEventPublisher kafkaPublisher,
                                    MeterRegistry meterRegistry) {
        this.documentCreateCounter = Counter.builder("knowledge.document.creates")
                .description("Knowledge document create count").register(meterRegistry);
        this.documentReadCounter = Counter.builder("knowledge.document.reads")
                .description("Knowledge document read count").register(meterRegistry);
        this.documentDeleteCounter = Counter.builder("knowledge.document.deletes")
                .description("Knowledge document delete count").register(meterRegistry);
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.versionRepository = versionRepository;
        this.chunkRepository = chunkRepository;
        this.metadataRepository = metadataRepository;
        this.tagRepository = tagRepository;
        this.accessLogRepository = accessLogRepository;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
    }

    public List<KnowledgeDocumentEntity> listDocuments() {
        return documentRepository.findByIsDeletedFalse();
    }

    public KnowledgeDocumentEntity getDocument(UUID id) {
        documentReadCounter.increment();
        return cacheService.getCachedDocument(id, () ->
                documentRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + id)));
    }

    @Transactional
    public KnowledgeDocumentEntity createDocument(UUID categoryId, String title, String content,
                                                   String description, String language, String author,
                                                   String source, String visibility, String businessModule,
                                                   String region, List<String> tags, UUID createdBy) {
        KnowledgeCategoryEntity category = categoryId != null
                ? categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                        .orElseThrow(() -> new KnowledgeNotFoundException("Category not found: " + categoryId))
                : null;

        if (title == null || title.isBlank()) {
            throw new KnowledgeValidationException("Document title is required");
        }

        KnowledgeDocumentEntity doc = new KnowledgeDocumentEntity(title, content, category);
        doc.setDescription(description);
        doc.setLanguage(language != null ? language : "en");
        doc.setAuthor(author);
        doc.setSource(source);
        doc.setVisibility(visibility != null ? visibility : "INTERNAL");
        doc.setBusinessModule(businessModule);
        doc.setRegion(region);
        doc.setCreatedBy(createdBy);
        doc.setCurrentVersion(1);
        doc = documentRepository.save(doc);

        if (tags != null) {
            for (String tag : tags) {
                KnowledgeTagEntity tagEntity = new KnowledgeTagEntity(doc, tag.trim().toLowerCase());
                tagRepository.save(tagEntity);
            }
        }

        KnowledgeDocumentVersionEntity version = new KnowledgeDocumentVersionEntity(doc, 1, content);
        version.setCreatedBy(createdBy);
        versionRepository.save(version);

        documentCreateCounter.increment();
        accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_CREATED"));
        kafkaPublisher.publishDocumentCreated(doc.getId(), doc.getTitle(), createdBy);
        cacheService.invalidateDocument(doc.getId());
        log.info("Created knowledge document: {} ({})", doc.getTitle(), doc.getId());
        return doc;
    }

    @Transactional
    public KnowledgeDocumentEntity updateDocument(UUID id, UUID categoryId, String title, String content,
                                                   String description, String language, String source,
                                                   String visibility, String businessModule,
                                                   String region, UUID updatedBy) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + id));

        if (categoryId != null) {
            KnowledgeCategoryEntity category = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                    .orElseThrow(() -> new KnowledgeNotFoundException("Category not found: " + categoryId));
            doc.setCategory(category);
        }
        if (title != null) doc.setTitle(title);
        if (content != null) doc.setContent(content);
        if (description != null) doc.setDescription(description);
        if (language != null) doc.setLanguage(language);
        if (source != null) doc.setSource(source);
        if (visibility != null) doc.setVisibility(visibility);
        if (businessModule != null) doc.setBusinessModule(businessModule);
        if (region != null) doc.setRegion(region);
        doc.setUpdatedBy(updatedBy);
        doc.setUpdatedAt(OffsetDateTime.now());

        if (content != null) {
            int newVersion = doc.getCurrentVersion() + 1;
            doc.setCurrentVersion(newVersion);
            KnowledgeDocumentVersionEntity version = new KnowledgeDocumentVersionEntity(doc, newVersion, content);
            version.setCreatedBy(updatedBy);
            versionRepository.save(version);
        }

        doc = documentRepository.save(doc);
        accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_UPDATED"));
        kafkaPublisher.publishDocumentUpdated(doc.getId(), updatedBy);
        cacheService.invalidateDocument(doc.getId());
        log.info("Updated knowledge document: {} ({})", doc.getTitle(), doc.getId());
        return doc;
    }

    @Transactional
    public void deleteDocument(UUID id, UUID deletedBy) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + id));
        doc.setDeleted(true);
        doc.setDeletedAt(OffsetDateTime.now());
        documentDeleteCounter.increment();
        documentRepository.save(doc);
        accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_DELETED"));
        kafkaPublisher.publishDocumentDeleted(id);
        cacheService.invalidateDocument(id);
        log.info("Deleted knowledge document: {} ({})", doc.getTitle(), id);
    }

    public List<KnowledgeDocumentEntity> searchDocuments(String query) {
        if (query == null || query.isBlank()) {
            return listDocuments();
        }
        Optional<String> cached = cacheService.getCachedSearchResults(query);
        if (cached.isPresent()) {
            return documentRepository.search(query);
        }
        List<KnowledgeDocumentEntity> results = documentRepository.search(query);
        kafkaPublisher.publishSearchExecuted(query, results.size());
        return results;
    }

    public List<KnowledgeDocumentEntity> findByCategory(UUID categoryId) {
        return documentRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
    }

    public List<KnowledgeDocumentEntity> findByBusinessModule(String businessModule) {
        return documentRepository.findByBusinessModuleAndIsDeletedFalse(businessModule);
    }

    public KnowledgeDocumentEntity publishDocument(UUID id, UUID publishedBy) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + id));
        doc.setStatus("PUBLISHED");
        doc.setUpdatedBy(publishedBy);
        doc.setUpdatedAt(OffsetDateTime.now());
        doc = documentRepository.save(doc);
        accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_PUBLISHED"));
        cacheService.invalidateDocument(doc.getId());
        return doc;
    }

    public KnowledgeDocumentEntity archiveDocument(UUID id, UUID archivedBy) {
        KnowledgeDocumentEntity doc = documentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new KnowledgeNotFoundException("Document not found: " + id));
        doc.setStatus("ARCHIVED");
        doc.setActive(false);
        doc.setUpdatedBy(archivedBy);
        doc.setUpdatedAt(OffsetDateTime.now());
        doc = documentRepository.save(doc);
        accessLogRepository.save(new KnowledgeAccessLogEntity(doc, "DOCUMENT_ARCHIVED"));
        cacheService.invalidateDocument(doc.getId());
        return doc;
    }

    public List<KnowledgeDocumentVersionEntity> listVersions(UUID documentId) {
        return versionRepository.findByDocumentIdAndIsDeletedFalseOrderByVersionNumberDesc(documentId);
    }

    public List<KnowledgeTagEntity> getDocumentTags(UUID documentId) {
        return tagRepository.findByDocumentId(documentId);
    }
}
