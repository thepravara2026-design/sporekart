package com.sporekart.ai.semantic.infrastructure.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class OpenAIEmbeddingAdapter extends EmbeddingProviderAdapter {

    private static final Logger log = LoggerFactory.getLogger(OpenAIEmbeddingAdapter.class);
    private static final int SMALL_DIMENSIONS = 1536;
    private static final int LARGE_DIMENSIONS = 3072;
    private static final int MAX_TOKENS = 8192;

    private final RandomGenerator rng;

    public OpenAIEmbeddingAdapter() {
        super("OPENAI", SMALL_DIMENSIONS, MAX_TOKENS, true);
        this.rng = RandomGenerator.getDefault();
    }

    @Override
    public List<Double> generate(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be null or blank");
        }
        log.debug("OpenAI embedding generated for text length={} (dimensions={})", text.length(), SMALL_DIMENSIONS);
        return generateRandomVector(SMALL_DIMENSIONS);
    }

    @Override
    public List<List<Double>> generateBatch(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }
        log.debug("OpenAI batch embedding: {} texts", texts.size());
        List<List<Double>> batch = new ArrayList<>();
        for (String text : texts) {
            batch.add(generate(text));
        }
        return batch;
    }

    public List<Double> generateWithModel(String text, String model) {
        int dims = "text-embedding-3-large".equals(model) ? LARGE_DIMENSIONS : SMALL_DIMENSIONS;
        log.debug("OpenAI embedding with model={} dims={}", model, dims);
        return generateRandomVector(dims);
    }

    private List<Double> generateRandomVector(int dimensions) {
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
