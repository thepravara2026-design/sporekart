package com.sporekart.bi.copilot.dto;

import java.util.List;
import java.util.Map;

public record BiQueryRequest(
    String query,
    String dataSource,
    Map<String, Object> filters,
    List<String> groupBy,
    List<String> metrics,
    String sortBy,
    String sortOrder,
    int page,
    int size
) {}
