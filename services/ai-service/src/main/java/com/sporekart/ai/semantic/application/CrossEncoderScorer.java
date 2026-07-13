package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CrossEncoderScorer {

    private static final Logger log = LoggerFactory.getLogger(CrossEncoderScorer.class);

    public List<SemanticSearchResult> rerank(List<SemanticSearchResult> candidates, String query) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        Map<String, Double> rerankedScores = new LinkedHashMap<>();
        for (SemanticSearchResult candidate : candidates) {
            double rerankScore = computeCrossEncoderScore(query, candidate.content());
            rerankedScores.put(candidate.documentId(), rerankScore);
        }

        log.debug("Cross-encoder re-ranked {} candidates for query '{}'", candidates.size(), truncate(query));
        return candidates.stream()
                .sorted(Comparator.comparingDouble((SemanticSearchResult r) ->
                        rerankedScores.getOrDefault(r.documentId(), r.score())).reversed())
                .peek(r -> {
                    double newScore = rerankedScores.getOrDefault(r.documentId(), r.score());
                    r = new SemanticSearchResult(r.documentId(), r.content(), newScore, 0, r.metadata());
                })
                .collect(Collectors.toList());
    }

    public List<SemanticSearchResult> rerankTopN(List<SemanticSearchResult> candidates, String query, int topN) {
        if (candidates.size() <= topN) {
            return rerank(candidates, query);
        }
        List<SemanticSearchResult> top = new ArrayList<>(candidates.subList(0, Math.min(topN, candidates.size())));
        return rerank(top, query);
    }

    private double computeCrossEncoderScore(String query, String content) {
        String q = query.toLowerCase();
        String c = content.toLowerCase();
        String[] queryTerms = q.split("\\s+");

        double exactMatchScore = c.contains(q) ? 0.3 : 0.0;

        double termOverlap = 0;
        for (String term : queryTerms) {
            if (c.contains(term)) {
                termOverlap += 1.0 / queryTerms.length;
            }
        }

        double proximityBonus = 0;
        if (queryTerms.length >= 2 && c.contains(q)) {
            proximityBonus = 0.2;
        }

        double result = exactMatchScore + termOverlap * 0.5 + proximityBonus;
        return Math.min(result, 1.0);
    }

    private String truncate(String s) {
        return s.length() <= 50 ? s : s.substring(0, 47) + "...";
    }
}
