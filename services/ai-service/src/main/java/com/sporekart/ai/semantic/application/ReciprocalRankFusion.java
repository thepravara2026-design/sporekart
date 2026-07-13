package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReciprocalRankFusion {

    private static final Logger log = LoggerFactory.getLogger(ReciprocalRankFusion.class);
    private static final int DEFAULT_K = 60;

    public List<SemanticSearchResult> fuse(List<List<SemanticSearchResult>> rankedLists, int k) {
        if (rankedLists == null || rankedLists.isEmpty()) {
            return List.of();
        }

        Map<String, Double> rrfScores = new LinkedHashMap<>();
        Map<String, SemanticSearchResult> resultMap = new LinkedHashMap<>();

        for (List<SemanticSearchResult> list : rankedLists) {
            int rank = 1;
            for (SemanticSearchResult result : list) {
                String id = result.documentId();
                double score = 1.0 / (k + rank);
                rrfScores.merge(id, score, Double::sum);
                resultMap.putIfAbsent(id, result);
                rank++;
            }
        }

        log.debug("RRF fusion: {} lists, {} unique results, k={}", rankedLists.size(), resultMap.size(), k);
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(rrfScores.entrySet());
        sorted.sort(Map.Entry.<String, Double>comparingByValue().reversed());
        return sorted.stream()
                .map(entry -> {
                    SemanticSearchResult r = resultMap.get(entry.getKey());
                    return new SemanticSearchResult(
                            r.documentId(), r.content(), entry.getValue(), 0, r.metadata());
                })
                .collect(Collectors.toList());
    }

    public List<SemanticSearchResult> fuse(List<List<SemanticSearchResult>> rankedLists) {
        return fuse(rankedLists, DEFAULT_K);
    }
}
