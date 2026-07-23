package com.sporekart.bi.copilot.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record NaturalLanguageQuery(
    String queryId,
    String originalQuery,
    String intent,
    String generatedQuery,
    List<String> dimensions,
    List<String> metrics,
    Map<String, Object> filters,
    String visualizationType,
    String explanation,
    OffsetDateTime queriedAt
) {}
