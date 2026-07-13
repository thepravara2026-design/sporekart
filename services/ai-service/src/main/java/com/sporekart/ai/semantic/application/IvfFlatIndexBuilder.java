package com.sporekart.ai.semantic.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class IvfFlatIndexBuilder {

    private static final Logger log = LoggerFactory.getLogger(IvfFlatIndexBuilder.class);
    private static final int MIN_LISTS = 1;
    private static final int MAX_LISTS = 4096;
    private static final int MIN_NPROBE = 1;
    private static final int MAX_NPROBE = 4096;
    private static final int DEFAULT_LISTS = 100;
    private static final int DEFAULT_NPROBE = 10;

    private final ObjectMapper objectMapper;

    public IvfFlatIndexBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String buildConfig(int lists, int nprobe) {
        if (lists < MIN_LISTS || lists > MAX_LISTS) {
            throw new IndexException("IVFFlat lists must be between " + MIN_LISTS + " and " + MAX_LISTS + ", got: " + lists);
        }
        if (nprobe < MIN_NPROBE || nprobe > MAX_NPROBE) {
            throw new IndexException("IVFFlat nprobe must be between " + MIN_NPROBE + " and " + MAX_NPROBE + ", got: " + nprobe);
        }

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("type", "ivfflat");
        config.put("lists", lists);
        config.put("nprobe", nprobe);

        try {
            String json = objectMapper.writeValueAsString(config);
            log.info("Built IVFFlat index config: lists={}, nprobe={}", lists, nprobe);
            return json;
        } catch (JsonProcessingException e) {
            throw new IndexException("Failed to serialize IVFFlat config", e);
        }
    }

    public String buildDefaultConfig() {
        return buildConfig(DEFAULT_LISTS, DEFAULT_NPROBE);
    }

    public static int getDefaultLists() {
        return DEFAULT_LISTS;
    }

    public static int getDefaultNprobe() {
        return DEFAULT_NPROBE;
    }
}
