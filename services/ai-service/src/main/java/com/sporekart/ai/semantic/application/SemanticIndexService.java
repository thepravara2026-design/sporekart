package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.IndexStatus;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticIndexStatisticsEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticIndexStatisticsRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SemanticIndexService {

    private static final Logger log = LoggerFactory.getLogger(SemanticIndexService.class);

    private final SemanticVectorIndexRepository indexRepository;
    private final SemanticIndexStatisticsRepository statisticsRepository;
    private final SemanticRedisCacheService cacheService;
    private final SemanticKafkaEventPublisher kafkaPublisher;

    public SemanticIndexService(SemanticVectorIndexRepository indexRepository,
                                SemanticIndexStatisticsRepository statisticsRepository,
                                SemanticRedisCacheService cacheService,
                                SemanticKafkaEventPublisher kafkaPublisher) {
        this.indexRepository = indexRepository;
        this.statisticsRepository = statisticsRepository;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @Transactional
    public SemanticVectorIndexEntity createIndex(String name, String description, int dimensions,
                                                   String indexConfig) {
        if (indexRepository.findByNameAndIsDeletedFalse(name).isPresent()) {
            throw new IndexException("Index already exists: " + name);
        }

        SemanticVectorIndexEntity index = new SemanticVectorIndexEntity();
        index.setName(name);
        index.setDescription(description);
        index.setStatus(IndexStatus.CREATING.name());
        index.setVectorCount(0);
        index.setDimensions(dimensions);
        index.setIndexConfig(indexConfig);
        index.setCreatedAt(OffsetDateTime.now());
        index = indexRepository.save(index);

        kafkaPublisher.publishVectorIndexBuilt(index.getId().toString());
        cacheService.invalidateIndexMetadata(name);
        log.info("Created vector index: {} (dimensions={})", name, dimensions);
        return index;
    }

    @Transactional
    public void rebuildIndex(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        index.setStatus(IndexStatus.REBUILDING.name());
        index.setUpdatedAt(OffsetDateTime.now());
        indexRepository.save(index);

        kafkaPublisher.publishVectorIndexRebuilt(index.getId().toString());
        cacheService.invalidateIndexMetadata(name);
        log.info("Rebuilding vector index: {}", name);
    }

    @Transactional
    public void optimizeIndex(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        index.setStatus(IndexStatus.OPTIMIZING.name());
        index.setUpdatedAt(OffsetDateTime.now());
        indexRepository.save(index);

        cacheService.invalidateIndexMetadata(name);
        log.info("Optimizing vector index: {}", name);
    }

    public IndexStatus getIndexStatus(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        return IndexStatus.valueOf(index.getStatus());
    }

    @Transactional
    public void activateIndex(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        index.setStatus(IndexStatus.ACTIVE.name());
        index.setUpdatedAt(OffsetDateTime.now());
        indexRepository.save(index);
        cacheService.invalidateIndexMetadata(name);
        log.info("Activated vector index: {}", name);
    }

    @Transactional
    public void deleteIndex(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        index.setDeleted(true);
        index.setDeletedAt(OffsetDateTime.now());
        indexRepository.save(index);
        cacheService.invalidateIndexMetadata(name);
        log.info("Deleted vector index: {}", name);
    }

    @Transactional
    public void incrementVectorCount(String name) {
        SemanticVectorIndexEntity index = indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
        index.setVectorCount(index.getVectorCount() + 1);
        indexRepository.save(index);
    }

    public Map<String, Double> getIndexStatistics(String name) {
        String cacheKey = name;
        Map<String, Double> cached = cacheService.getCachedStatistics(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<SemanticIndexStatisticsEntity> stats = statisticsRepository.findByIndexNameAndIsDeletedFalse(name);
        Map<String, Double> result = stats.stream()
                .collect(Collectors.toMap(SemanticIndexStatisticsEntity::getStatKey,
                        SemanticIndexStatisticsEntity::getStatValue));
        cacheService.cacheStatistics(cacheKey, result);
        return result;
    }

    @Transactional
    public void recordStatistics(String indexName, String statKey, double statValue, String metadata) {
        SemanticIndexStatisticsEntity stat = new SemanticIndexStatisticsEntity();
        stat.setIndexName(indexName);
        stat.setStatKey(statKey);
        stat.setStatValue(statValue);
        stat.setMetadata(metadata);
        stat.setRecordedAt(OffsetDateTime.now());
        stat.setCreatedAt(OffsetDateTime.now());
        statisticsRepository.save(stat);
        cacheService.invalidateStatistics(indexName);
    }

    public List<SemanticVectorIndexEntity> listIndexes() {
        return indexRepository.findByIsDeletedFalse();
    }

    public SemanticVectorIndexEntity getIndex(UUID id) {
        return indexRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IndexException("Index not found: " + id));
    }

    public SemanticVectorIndexEntity getIndexByName(String name) {
        return indexRepository.findByNameAndIsDeletedFalse(name)
                .orElseThrow(() -> new IndexException("Index not found: " + name));
    }
}
