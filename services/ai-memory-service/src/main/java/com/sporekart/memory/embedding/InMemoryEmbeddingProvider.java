package com.sporekart.memory.embedding;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "sporekart.memory.vector-provider", havingValue = "MEMORY", matchIfMissing = true)
public class InMemoryEmbeddingProvider implements EmbeddingProvider {

    private final Map<UUID, float[]> embeddings = new ConcurrentHashMap<>();
    private final Map<UUID, String> contents = new ConcurrentHashMap<>();
    private final Random random = new Random(42);

    @Override
    public String providerName() { return "MEMORY"; }

    @Override
    public EmbeddingResult generateEmbedding(String text) {
        var id = UUID.randomUUID();
        var vector = generateRandomVector(1536);
        embeddings.put(id, vector);
        contents.put(id, text);
        return new EmbeddingResult(id, vector, 1536, 5, "memory-in-memory");
    }

    @Override
    public List<EmbeddingResult> generateEmbeddings(List<String> texts) {
        return texts.stream().map(this::generateEmbedding).toList();
    }

    @Override
    public boolean deleteEmbedding(UUID embeddingId) {
        embeddings.remove(embeddingId);
        contents.remove(embeddingId);
        return true;
    }

    @Override
    public Optional<EmbeddingResult> updateEmbedding(UUID embeddingId, String text) {
        deleteEmbedding(embeddingId);
        return Optional.of(generateEmbedding(text));
    }

    @Override
    public List<SimilarityResult> searchSimilar(float[] vector, int maxResults, double minScore) {
        return embeddings.entrySet().stream()
                .map(e -> {
                    double score = cosineSimilarity(vector, e.getValue());
                    return new SimilarityResult(e.getKey(), e.getKey(), score, contents.get(e.getKey()));
                })
                .filter(r -> r.score() >= minScore)
                .sorted(Comparator.comparingDouble(SimilarityResult::score).reversed())
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    @Override
    public int dimensions() { return 1536; }

    @Override
    public boolean isAvailable() { return true; }

    private float[] generateRandomVector(int dims) {
        var vec = new float[dims];
        for (int i = 0; i < dims; i++) {
            vec[i] = (float) (random.nextGaussian() * 0.1);
        }
        return vec;
    }

    private double cosineSimilarity(float[] a, float[] b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }
}
