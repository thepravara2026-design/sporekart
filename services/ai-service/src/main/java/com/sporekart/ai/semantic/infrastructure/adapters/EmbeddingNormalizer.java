package com.sporekart.ai.semantic.infrastructure.adapters;

import java.util.ArrayList;
import java.util.List;

public final class EmbeddingNormalizer {

    private EmbeddingNormalizer() {}

    public static List<Double> l2Normalize(List<Double> vector) {
        if (vector == null || vector.isEmpty()) {
            return vector;
        }
        double sumSquares = 0;
        for (double v : vector) {
            sumSquares += v * v;
        }
        double norm = Math.sqrt(sumSquares);
        if (norm == 0) {
            return new ArrayList<>(vector);
        }
        List<Double> normalized = new ArrayList<>(vector.size());
        for (double v : vector) {
            normalized.add(v / norm);
        }
        return normalized;
    }

    public static List<Double> minMaxScale(List<Double> vector) {
        if (vector == null || vector.isEmpty()) {
            return vector;
        }
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        for (double v : vector) {
            if (v < min) min = v;
            if (v > max) max = v;
        }
        double range = max - min;
        if (range == 0) {
            return new ArrayList<>(vector);
        }
        List<Double> scaled = new ArrayList<>(vector.size());
        for (double v : vector) {
            scaled.add((v - min) / range);
        }
        return scaled;
    }

    public static double cosineSimilarity(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.size() != b.size() || a.isEmpty()) {
            return 0;
        }
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        return denom == 0 ? 0 : dot / denom;
    }

    public static double euclideanDistance(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.size() != b.size() || a.isEmpty()) {
            return Double.MAX_VALUE;
        }
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            double diff = a.get(i) - b.get(i);
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    public static double dotProduct(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.size() != b.size() || a.isEmpty()) {
            return 0;
        }
        double dot = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
        }
        return dot;
    }
}
