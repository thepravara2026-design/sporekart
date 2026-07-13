package com.sporekart.ai.semantic.api;

import com.sporekart.ai.semantic.domain.RankingStrategy;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;

import java.util.List;
import java.util.Map;

public interface SemanticRankingService {
    List<SemanticSearchResult> rank(List<SemanticSearchResult> results, RankingStrategy strategy, Map<String, Object> params);
}
