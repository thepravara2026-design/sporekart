package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.EmbeddingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingValidationService implements EmbeddingValidator {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingValidationService.class);
    private static final int MAX_CONTENT_LENGTH = 100000;
    private static final int MIN_CONTENT_LENGTH = 1;

    @Override
    public boolean validate(List<Double> embedding, int expectedDimensions) {
        if (embedding == null || embedding.isEmpty()) {
            log.warn("Embedding validation failed: null or empty");
            return false;
        }
        if (expectedDimensions > 0 && embedding.size() != expectedDimensions) {
            log.warn("Embedding validation failed: expected {} dimensions, got {}", expectedDimensions, embedding.size());
            return false;
        }
        return true;
    }

    @Override
    public boolean validateContent(String content) {
        if (content == null || content.isBlank()) {
            log.warn("Content validation failed: null or blank");
            return false;
        }
        if (content.length() < MIN_CONTENT_LENGTH) {
            log.warn("Content validation failed: too short ({} chars)", content.length());
            return false;
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            log.warn("Content validation failed: too long ({} chars, max {})", content.length(), MAX_CONTENT_LENGTH);
            return false;
        }
        return true;
    }
}
