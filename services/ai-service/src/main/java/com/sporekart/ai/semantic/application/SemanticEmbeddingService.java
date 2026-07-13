package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.EmbeddingGenerator;
import com.sporekart.ai.semantic.domain.EmbeddingStatus;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.adapters.ProviderRoutingEmbeddingGenerator;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
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
public class SemanticEmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(SemanticEmbeddingService.class);

    private final SemanticEmbeddingRepository embeddingRepository;
    private final SemanticRedisCacheService cacheService;
    private final SemanticKafkaEventPublisher kafkaPublisher;
    private final EmbeddingGenerator embeddingGenerator;
    private final Counter createCounter;
    private final Counter readCounter;
    private final Counter deleteCounter;

    public SemanticEmbeddingService(SemanticEmbeddingRepository embeddingRepository,
                                    SemanticRedisCacheService cacheService,
                                    SemanticKafkaEventPublisher kafkaPublisher,
                                    ProviderRoutingEmbeddingGenerator embeddingGenerator,
                                    MeterRegistry meterRegistry) {
        this.embeddingRepository = embeddingRepository;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
        this.embeddingGenerator = embeddingGenerator;
        this.createCounter = Counter.builder("semantic.embedding.creates")
                .description("Semantic embedding create count").register(meterRegistry);
        this.readCounter = Counter.builder("semantic.embedding.reads")
                .description("Semantic embedding read count").register(meterRegistry);
        this.deleteCounter = Counter.builder("semantic.embedding.deletes")
                .description("Semantic embedding delete count").register(meterRegistry);
    }

    public List<SemanticEmbeddingEntity> listEmbeddings() {
        return embeddingRepository.findByIsDeletedFalse();
    }

    public SemanticEmbeddingEntity getEmbedding(UUID id) {
        readCounter.increment();
        return cacheService.getCachedEmbeddingMetadata(id, () ->
                embeddingRepository.findByIdAndIsDeletedFalse(id)
                        .orElseThrow(() -> new EmbeddingException("Embedding not found: " + id)));
    }

    @Transactional
    public SemanticEmbeddingEntity createEmbedding(String content, String embedding, String provider,
                                                    String model, int dimensions, String status,
                                                    UUID createdBy) {
        if (content == null || content.isBlank()) {
            throw new EmbeddingException("Content is required for embedding");
        }

        SemanticEmbeddingEntity entity = new SemanticEmbeddingEntity();
        entity.setContent(content);
        entity.setEmbedding(embedding);
        entity.setProvider(provider);
        entity.setModel(model);
        entity.setDimensions(dimensions);
        entity.setStatus(status != null ? status : EmbeddingStatus.PENDING.name());
        entity.setVersion(1);
        entity.setCreatedBy(createdBy);
        entity.setCreatedAt(OffsetDateTime.now());
        entity = embeddingRepository.save(entity);

        createCounter.increment();
        kafkaPublisher.publishEmbeddingCreated(entity.getId().toString(), provider, createdBy);
        cacheService.invalidateEmbeddingMetadata(entity.getId());
        log.info("Created semantic embedding: {} (provider={})", entity.getId(), provider);
        return entity;
    }

    @Transactional
    public SemanticEmbeddingEntity updateEmbedding(UUID id, String content, String embedding,
                                                    String provider, String model, Integer dimensions,
                                                    String status, UUID updatedBy) {
        SemanticEmbeddingEntity entity = embeddingRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EmbeddingException("Embedding not found: " + id));

        if (content != null) entity.setContent(content);
        if (embedding != null) entity.setEmbedding(embedding);
        if (provider != null) entity.setProvider(provider);
        if (model != null) entity.setModel(model);
        if (dimensions != null) entity.setDimensions(dimensions);
        if (status != null) entity.setStatus(status);
        entity.setVersion(entity.getVersion() + 1);
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(OffsetDateTime.now());
        entity = embeddingRepository.save(entity);

        kafkaPublisher.publishEmbeddingUpdated(entity.getId().toString(), updatedBy);
        cacheService.invalidateEmbeddingMetadata(entity.getId());
        log.info("Updated semantic embedding: {}", entity.getId());
        return entity;
    }

    @Transactional
    public void deleteEmbedding(UUID id, UUID deletedBy) {
        SemanticEmbeddingEntity entity = embeddingRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EmbeddingException("Embedding not found: " + id));
        entity.setDeleted(true);
        entity.setDeletedAt(OffsetDateTime.now());
        deleteCounter.increment();
        embeddingRepository.save(entity);
        kafkaPublisher.publishEmbeddingDeleted(entity.getId().toString());
        cacheService.invalidateEmbeddingMetadata(id);
        log.info("Deleted semantic embedding: {}", id);
    }

    public SemanticEmbeddingEntity generateAndStoreEmbedding(String content, String provider,
                                                               String model, UUID createdBy) {
        List<Double> vector = embeddingGenerator.generate(content, provider, model);
        int dimensions = vector.size();
        String embeddingJson = vector.toString();
        return createEmbedding(content, embeddingJson, provider, model, dimensions,
                EmbeddingStatus.COMPLETED.name(), createdBy);
    }

    public Optional<SemanticEmbeddingEntity> findByContent(String content) {
        return embeddingRepository.findByContentAndIsDeletedFalse(content);
    }

    public List<SemanticEmbeddingEntity> findByProvider(String provider) {
        return embeddingRepository.findByProviderAndIsDeletedFalse(provider);
    }

    public List<SemanticEmbeddingEntity> findByStatus(String status) {
        return embeddingRepository.findByStatusAndIsDeletedFalse(status);
    }
}
