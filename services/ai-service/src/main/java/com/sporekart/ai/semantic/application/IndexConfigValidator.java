package com.sporekart.ai.semantic.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IndexConfigValidator {

    private static final Logger log = LoggerFactory.getLogger(IndexConfigValidator.class);
    private static final int MIN_DIMENSIONS = 64;
    private static final int MAX_DIMENSIONS = 4096;

    private final ObjectMapper objectMapper;

    public IndexConfigValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ValidationResult validateConfig(String configJson) {
        if (configJson == null || configJson.isBlank()) {
            return ValidationResult.valid();
        }

        try {
            Map<String, Object> config = objectMapper.readValue(configJson, Map.class);
            String type = (String) config.getOrDefault("type", "hnsw");

            if (type.equalsIgnoreCase("hnsw")) {
                return validateHnswConfig(config);
            } else if (type.equalsIgnoreCase("ivfflat")) {
                return validateIvfFlatConfig(config);
            } else {
                return ValidationResult.invalid("Unknown index type: " + type);
            }
        } catch (JsonProcessingException e) {
            return ValidationResult.invalid("Invalid JSON config: " + e.getMessage());
        }
    }

    public boolean validateDimensions(int dimensions) {
        if (dimensions < MIN_DIMENSIONS) {
            log.warn("Dimensions too low: {} (min {})", dimensions, MIN_DIMENSIONS);
            return false;
        }
        if (dimensions > MAX_DIMENSIONS) {
            log.warn("Dimensions too high: {} (max {})", dimensions, MAX_DIMENSIONS);
            return false;
        }
        return true;
    }

    private ValidationResult validateHnswConfig(Map<String, Object> config) {
        if (config.containsKey("m")) {
            int m = ((Number) config.get("m")).intValue();
            if (m < 4 || m > 64) {
                return ValidationResult.invalid("HNSW m must be between 4 and 64");
            }
        }
        if (config.containsKey("efConstruction")) {
            int ef = ((Number) config.get("efConstruction")).intValue();
            if (ef < 4 || ef > 512) {
                return ValidationResult.invalid("HNSW efConstruction must be between 4 and 512");
            }
        }
        return ValidationResult.valid();
    }

    private ValidationResult validateIvfFlatConfig(Map<String, Object> config) {
        if (config.containsKey("lists")) {
            int lists = ((Number) config.get("lists")).intValue();
            if (lists < 1 || lists > 4096) {
                return ValidationResult.invalid("IVFFlat lists must be between 1 and 4096");
            }
        }
        if (config.containsKey("nprobe")) {
            int nprobe = ((Number) config.get("nprobe")).intValue();
            if (nprobe < 1 || nprobe > 4096) {
                return ValidationResult.invalid("IVFFlat nprobe must be between 1 and 4096");
            }
        }
        return ValidationResult.valid();
    }

    public record ValidationResult(boolean isValid, String message) {
        public static ValidationResult valid() {
            return new ValidationResult(true, "OK");
        }

        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return isValid;
        }
    }
}
