package com.sporekart.operations.copilot.dto;

import java.util.List;
import java.util.Map;

public record OperationsQueryResponse(
    String queryId,
    String intent,
    String summary,
    Map<String, Object> data,
    List<String> recommendations,
    List<String> alerts,
    long processingTimeMs
) {}
