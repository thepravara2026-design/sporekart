package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.SemanticRankingService;
import com.sporekart.ai.semantic.domain.RankingStrategy;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SemanticRankingServiceImpl implements SemanticRankingService {

    private static final Logger log = LoggerFactory.getLogger(SemanticRankingServiceImpl.class);

    @Override
    public List<SemanticSearchResult> rank(List<SemanticSearchResult> results, RankingStrategy strategy,
                                            Map<String, Object> params) {
        if (results == null || results.isEmpty()) {
            return List.of();
        }

        List<SemanticSearchResult> ranked = switch (strategy) {
            case SIMILARITY -> rankBySimilarity(results);
            case KEYWORD_WEIGHT -> rankByKeywordWeight(results, params);
            case METADATA_BOOST -> rankByMetadataBoost(results, params);
            case FRESHNESS -> rankByFreshness(results, params);
            case BUSINESS_PRIORITY -> rankByBusinessPriority(results, params);
            case CATEGORY_WEIGHT -> rankByCategoryWeight(results, params);
            case LANGUAGE_PREFERENCE -> rankByLanguagePreference(results, params);
            case HYBRID -> rankHybrid(results, params);
        };

        log.debug("Ranked {} results using strategy {}", ranked.size(), strategy);
        return ranked;
    }

    private List<SemanticSearchResult> rankBySimilarity(List<SemanticSearchResult> results) {
        return results.stream()
                .sorted(Comparator.comparingDouble(SemanticSearchResult::score).reversed())
                .collect(Collectors.toList());
    }

    private List<SemanticSearchResult> rankByKeywordWeight(List<SemanticSearchResult> results,
                                                            Map<String, Object> params) {
        double keywordWeight = params.containsKey("keywordWeight")
                ? ((Number) params.get("keywordWeight")).doubleValue() : 0.3;
        return results.stream()
                .sorted(Comparator.comparingDouble(r -> r.score() * (1 + keywordWeight)))
                .collect(Collectors.toList());
    }

    private List<SemanticSearchResult> rankByMetadataBoost(List<SemanticSearchResult> results,
                                                            Map<String, Object> params) {
        String boostKey = params.containsKey("boostKey") ? params.get("boostKey").toString() : null;
        if (boostKey == null) return rankBySimilarity(results);

        Comparator<SemanticSearchResult> byBoost = Comparator.comparingDouble((SemanticSearchResult r) -> {
            String val = r.metadata().getOrDefault(boostKey, "");
            double boost = val.isEmpty() ? 0 : 0.2;
            return r.score() + boost;
        });
        return results.stream()
                .sorted(byBoost.reversed())
                .collect(Collectors.toList());
    }

    private List<SemanticSearchResult> rankByFreshness(List<SemanticSearchResult> results,
                                                        Map<String, Object> params) {
        return rankBySimilarity(results);
    }

    private List<SemanticSearchResult> rankByBusinessPriority(List<SemanticSearchResult> results,
                                                               Map<String, Object> params) {
        return rankBySimilarity(results);
    }

    private List<SemanticSearchResult> rankByCategoryWeight(List<SemanticSearchResult> results,
                                                             Map<String, Object> params) {
        return rankBySimilarity(results);
    }

    private List<SemanticSearchResult> rankByLanguagePreference(List<SemanticSearchResult> results,
                                                                 Map<String, Object> params) {
        return rankBySimilarity(results);
    }

    private List<SemanticSearchResult> rankHybrid(List<SemanticSearchResult> results,
                                                   Map<String, Object> params) {
        double semanticWeight = params.containsKey("semanticWeight")
                ? ((Number) params.get("semanticWeight")).doubleValue() : 0.7;
        double keywordWeight = params.containsKey("keywordWeight")
                ? ((Number) params.get("keywordWeight")).doubleValue() : 0.3;

        return results.stream()
                .sorted(Comparator.comparingDouble(r -> r.score() * semanticWeight + keywordWeight))
                .collect(Collectors.toList());
    }
}
