package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScoreNormalizer {

    public List<SemanticSearchResult> normalize(List<SemanticSearchResult> results, double min, double max) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        double actualMin = results.stream().mapToDouble(SemanticSearchResult::score).min().orElse(0);
        double actualMax = results.stream().mapToDouble(SemanticSearchResult::score).max().orElse(1);
        double range = actualMax - actualMin;

        if (range == 0) {
            return results.stream()
                    .map(r -> new SemanticSearchResult(r.documentId(), r.content(), (min + max) / 2, r.rank(), r.metadata()))
                    .collect(Collectors.toList());
        }

        double rangeTarget = max - min;
        return results.stream()
                .map(r -> {
                    double normalized = min + ((r.score() - actualMin) / range) * rangeTarget;
                    return new SemanticSearchResult(r.documentId(), r.content(), normalized, r.rank(), r.metadata());
                })
                .collect(Collectors.toList());
    }

    public List<SemanticSearchResult> normalizeTo01(List<SemanticSearchResult> results) {
        return normalize(results, 0.0, 1.0);
    }
}
