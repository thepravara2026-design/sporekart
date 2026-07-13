package com.sporekart.ai.semantic.api;

import java.util.List;

public interface EmbeddingValidator {
    boolean validate(List<Double> embedding, int expectedDimensions);
    boolean validateContent(String content);
}
