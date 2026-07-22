package com.sporekart.memory.dto;

import java.util.List;

public record SearchMemoriesRequest(
    String query,
    List<String> tags,
    List<String> workspaces,
    List<String> memoryTypes,
    List<String> sources,
    int maxResults,
    double minRelevanceScore
) {}
