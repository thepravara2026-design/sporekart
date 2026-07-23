package com.sporekart.executive.copilot.dto;

import java.util.List;
import java.util.Map;

public record ExecutiveQueryResponse(
    String queryId,
    String intent,
    String executiveSummary,
    Map<String, Object> data,
    List<String> insights,
    List<String> recommendations,
    long processingTimeMs
) {}
