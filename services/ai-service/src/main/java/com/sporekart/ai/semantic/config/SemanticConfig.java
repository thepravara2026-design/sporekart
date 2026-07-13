package com.sporekart.ai.semantic.config;

import com.sporekart.ai.semantic.domain.IndexStatus;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticVectorIndexRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
@ConfigurationProperties(prefix = "semantic")
public class SemanticConfig {

    private static final Logger log = LoggerFactory.getLogger(SemanticConfig.class);

    private final SemanticVectorIndexRepository indexRepository;

    private double defaultThreshold = 0.7;
    private int defaultLimit = 10;
    private int maxBatchSize = 100;
    private int cacheTtlMinutes = 30;
    private int embeddingCacheTtlMinutes = 60;
    private int searchCacheTtlMinutes = 5;
    private int similarityCacheTtlMinutes = 10;
    private int indexCacheTtlMinutes = 30;
    private int statisticsCacheTtlMinutes = 5;

    public SemanticConfig(SemanticVectorIndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    @PostConstruct
    public void initializeDefaults() {
        if (indexRepository.findByNameAndIsDeletedFalse("default").isEmpty()) {
            SemanticVectorIndexEntity defaultIndex = new SemanticVectorIndexEntity();
            defaultIndex.setName("default");
            defaultIndex.setDescription("Default vector index for semantic search");
            defaultIndex.setStatus(IndexStatus.CREATING.name());
            defaultIndex.setVectorCount(0);
            defaultIndex.setDimensions(1536);
            defaultIndex.setIndexConfig("{\"metric\": \"cosine\", \"nlist\": 100}");
            defaultIndex.setCreatedAt(OffsetDateTime.now());
            indexRepository.save(defaultIndex);
            log.info("Created default vector index");
        }
    }

    public double getDefaultThreshold() { return defaultThreshold; }
    public void setDefaultThreshold(double defaultThreshold) { this.defaultThreshold = defaultThreshold; }

    public int getDefaultLimit() { return defaultLimit; }
    public void setDefaultLimit(int defaultLimit) { this.defaultLimit = defaultLimit; }

    public int getMaxBatchSize() { return maxBatchSize; }
    public void setMaxBatchSize(int maxBatchSize) { this.maxBatchSize = maxBatchSize; }

    public int getCacheTtlMinutes() { return cacheTtlMinutes; }
    public void setCacheTtlMinutes(int cacheTtlMinutes) { this.cacheTtlMinutes = cacheTtlMinutes; }

    public int getEmbeddingCacheTtlMinutes() { return embeddingCacheTtlMinutes; }
    public void setEmbeddingCacheTtlMinutes(int embeddingCacheTtlMinutes) { this.embeddingCacheTtlMinutes = embeddingCacheTtlMinutes; }

    public int getSearchCacheTtlMinutes() { return searchCacheTtlMinutes; }
    public void setSearchCacheTtlMinutes(int searchCacheTtlMinutes) { this.searchCacheTtlMinutes = searchCacheTtlMinutes; }

    public int getSimilarityCacheTtlMinutes() { return similarityCacheTtlMinutes; }
    public void setSimilarityCacheTtlMinutes(int similarityCacheTtlMinutes) { this.similarityCacheTtlMinutes = similarityCacheTtlMinutes; }

    public int getIndexCacheTtlMinutes() { return indexCacheTtlMinutes; }
    public void setIndexCacheTtlMinutes(int indexCacheTtlMinutes) { this.indexCacheTtlMinutes = indexCacheTtlMinutes; }

    public int getStatisticsCacheTtlMinutes() { return statisticsCacheTtlMinutes; }
    public void setStatisticsCacheTtlMinutes(int statisticsCacheTtlMinutes) { this.statisticsCacheTtlMinutes = statisticsCacheTtlMinutes; }
}
