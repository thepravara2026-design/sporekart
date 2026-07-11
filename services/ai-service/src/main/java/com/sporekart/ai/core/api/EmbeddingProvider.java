package com.sporekart.ai.core.api;

import java.util.List;

public interface EmbeddingProvider {
    List<Float> embed(String text, String model);
    List<List<Float>> embedBatch(List<String> texts, String model);
    int getEmbeddingDimension(String model);
}
