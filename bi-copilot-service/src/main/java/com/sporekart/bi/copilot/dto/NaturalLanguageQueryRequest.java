package com.sporekart.bi.copilot.dto;

public record NaturalLanguageQueryRequest(
    String query,
    boolean generateVisualization,
    boolean includeExplanation
) {}