package com.sporekart.bi.copilot.dto;

import java.util.List;
import java.util.Map;

public record BiQueryResponse(
    List<Map<String, Object>> results,
    int total,
    int page,
    int size,
    long queryTimeMs,
    String queryExplanation
) {}
