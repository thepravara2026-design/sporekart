package com.sporekart.ai.semantic.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class HnswIndexBuilder {

    private static final Logger log = LoggerFactory.getLogger(HnswIndexBuilder.class);
    private static final int MIN_M = 4;
    private static final int MAX_M = 64;
    private static final int MIN_EF_CONSTRUCTION = 4;
    private static final int MAX_EF_CONSTRUCTION = 512;
    private static final int DEFAULT_M = 16;
    private static final int DEFAULT_EF_CONSTRUCTION = 200;

    private final ObjectMapper objectMapper;

    public HnswIndexBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String buildConfig(int m, int efConstruction) {
        if (m < MIN_M || m > MAX_M) {
            throw new IndexException("HNSW M must be between " + MIN_M + " and " + MAX_M + ", got: " + m);
        }
        if (efConstruction < MIN_EF_CONSTRUCTION || efConstruction > MAX_EF_CONSTRUCTION) {
            throw new IndexException("HNSW efConstruction must be between " + MIN_EF_CONSTRUCTION + " and " + MAX_EF_CONSTRUCTION + ", got: " + efConstruction);
        }

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("type", "hnsw");
        config.put("m", m);
        config.put("efConstruction", efConstruction);

        try {
            String json = objectMapper.writeValueAsString(config);
            log.info("Built HNSW index config: m={}, efConstruction={}", m, efConstruction);
            return json;
        } catch (JsonProcessingException e) {
            throw new IndexException("Failed to serialize HNSW config", e);
        }
    }

    public String buildDefaultConfig() {
        return buildConfig(DEFAULT_M, DEFAULT_EF_CONSTRUCTION);
    }

    public static int getDefaultM() {
        return DEFAULT_M;
    }

    public static int getDefaultEfConstruction() {
        return DEFAULT_EF_CONSTRUCTION;
    }
}
