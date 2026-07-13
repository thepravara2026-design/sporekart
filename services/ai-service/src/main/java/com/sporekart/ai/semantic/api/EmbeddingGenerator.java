package com.sporekart.ai.semantic.api;

import java.util.List;

public interface EmbeddingGenerator {
    List<Double> generate(String content, String provider, String model);
    List<List<Double>> generateBatch(List<String> contents, String provider);
}
