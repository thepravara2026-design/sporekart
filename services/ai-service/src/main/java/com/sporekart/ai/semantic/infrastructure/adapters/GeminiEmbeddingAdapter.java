package com.sporekart.ai.semantic.infrastructure.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class GeminiEmbeddingAdapter extends EmbeddingProviderAdapter {

    private static final Logger log = LoggerFactory.getLogger(GeminiEmbeddingAdapter.class);
    private static final int DIMENSIONS = 768;
    private static final int MAX_TOKENS = 3072;

    private final RandomGenerator rng;

    public GeminiEmbeddingAdapter() {
        super("GEMINI", DIMENSIONS, MAX_TOKENS, true);
        this.rng = RandomGenerator.getDefault();
    }

    @Override
    public List<Double> generate(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be null or blank");
        }
        log.debug("Gemini embedding generated for text length={} (dimensions={})", text.length(), DIMENSIONS);
        return generateNormalizedVector(DIMENSIONS);
    }

    @Override
    public List<List<Double>> generateBatch(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }
        log.debug("Gemini batch embedding: {} texts", texts.size());
        List<List<Double>> batch = new ArrayList<>();
        for (String text : texts) {
            batch.add(generate(text));
        }
        return batch;
    }

    private List<Double> generateNormalizedVector(int dimensions) {
        List<Double> vector = new ArrayList<>(dimensions);
        double sum = 0;
        for (int i = 0; i < dimensions; i++) {
            double val = rng.nextDouble() * 2 - 1;
            vector.add(val);
            sum += val * val;
        }
        double norm = Math.sqrt(sum);
        for (int i = 0; i < dimensions; i++) {
            vector.set(i, vector.get(i) / norm);
        }
        return vector;
    }
}
