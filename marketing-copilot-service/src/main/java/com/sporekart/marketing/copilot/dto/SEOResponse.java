package com.sporekart.marketing.copilot.dto;

import java.util.List;

public record SEOResponse(
    String url,
    String targetKeyword,
    int searchVolume,
    double keywordDifficulty,
    int currentRanking,
    List<String> suggestions,
    SEOChecklistResponse checklist,
    double estimatedTraffic,
    String locale
) {
    public record SEOChecklistResponse(double score, List<String> critical, List<String> warnings, List<String> recommendations) {}
}
