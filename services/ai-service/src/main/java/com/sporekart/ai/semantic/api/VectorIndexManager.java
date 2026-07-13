package com.sporekart.ai.semantic.api;

import com.sporekart.ai.semantic.domain.IndexStatus;

import java.util.Map;

public interface VectorIndexManager {
    void createIndex(String name, int dimensions);
    void rebuildIndex(String name);
    void optimizeIndex(String name);
    IndexStatus getIndexStatus(String name);
    void deleteIndex(String name);
    Map<String, Double> getIndexStatistics(String name);
}
