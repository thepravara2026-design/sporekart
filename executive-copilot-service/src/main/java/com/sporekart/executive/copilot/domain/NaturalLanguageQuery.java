package com.sporekart.executive.copilot.domain;

import java.util.List;
import java.util.Map;

public record NaturalLanguageQuery(
    String queryId,
    String originalQuery,
    String intent,
    String summary,
    Map<String, Object> data,
    List<String> insights,
    List<String> recommendations
) {}
