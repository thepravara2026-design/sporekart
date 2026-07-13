package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HybridScorer {

    private static final Logger log = LoggerFactory.getLogger(HybridScorer.class);

    public List<SemanticSearchResult> fuse(List<SemanticSearchResult> semanticResults,
                                            List<SemanticSearchResult> keywordResults,
                                            double semanticWeight, double keywordWeight, int k) {
        Map<String, FusedEntry> fused = new LinkedHashMap<>();

        for (SemanticSearchResult r : semanticResults) {
            fused.put(r.documentId(), new FusedEntry(r, r.rank()));
        }

        for (SemanticSearchResult r : keywordResults) {
            fused.merge(r.documentId(), new FusedEntry(r, r.rank()),
                    (existing, incoming) -> {
                        existing.rrfScore = existing.rrfScore + (1.0 / (k + incoming.initialRank));
                        return existing;
                    });
        }

        for (Map.Entry<String, FusedEntry> entry : fused.entrySet()) {
            FusedEntry fe = entry.getValue();
            if (fe.rrfScore == 0) {
                fe.rrfScore = 1.0 / (k + fe.initialRank);
            }
        }

        return fused.values().stream()
                .sorted(Comparator.comparingDouble((FusedEntry e) -> e.rrfScore).reversed())
                .map(e -> new SemanticSearchResult(
                        e.result.documentId(),
                        e.result.content(),
                        e.rrfScore,
                        0,
                        e.result.metadata()))
                .collect(Collectors.toList());
    }

    public List<SemanticSearchResult> weightedFusion(List<SemanticSearchResult> semanticResults,
                                                      List<SemanticSearchResult> keywordResults,
                                                      double semanticWeight, double keywordWeight) {
        Map<String, SemanticSearchResult> merged = new LinkedHashMap<>();

        for (SemanticSearchResult r : semanticResults) {
            merged.put(r.documentId(), new SemanticSearchResult(
                    r.documentId(), r.content(), r.score() * semanticWeight, r.rank(), r.metadata()));
        }

        for (SemanticSearchResult r : keywordResults) {
            merged.merge(r.documentId(), new SemanticSearchResult(
                            r.documentId(), r.content(), r.score() * keywordWeight, r.rank(), r.metadata()),
                    (a, b) -> new SemanticSearchResult(
                            a.documentId(), a.content(), a.score() + b.score(), 0, a.metadata()));
        }

        return merged.values().stream()
                .sorted(Comparator.comparingDouble(SemanticSearchResult::score).reversed())
                .collect(Collectors.toList());
    }

    private static class FusedEntry {
        final SemanticSearchResult result;
        final int initialRank;
        double rrfScore;

        FusedEntry(SemanticSearchResult result, int initialRank) {
            this.result = result;
            this.initialRank = initialRank;
            this.rrfScore = 1.0 / (60.0 + initialRank);
        }
    }
}
