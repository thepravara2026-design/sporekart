package com.sporekart.marketing.copilot.dto;

import java.util.List;
import java.util.Map;

public record MarketingQueryResponse(
    String queryId,
    String intent,
    String summary,
    Map<String, Object> data,
    List<String> recommendations,
    long processingTimeMs
) {}
