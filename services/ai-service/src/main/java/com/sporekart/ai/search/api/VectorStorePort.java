package com.sporekart.ai.search.api;

import java.util.List;
import java.util.Map;

public interface VectorStorePort {
    void store(String id, List<Float> embedding, Map<String, Object> metadata);
    List<String> search(List<Float> queryEmbedding, int topK);
    void delete(String id);
}
