package com.sporekart.marketing.copilot.domain;

import java.util.List;

public record SEOResult(
    String url,
    String targetKeyword,
    int searchVolume,
    double keywordDifficulty,
    int currentRanking,
    int targetRanking,
    List<String> suggestions,
    String contentType,
    List<String> relatedKeywords,
    double estimatedTraffic,
    String locale
) {}
