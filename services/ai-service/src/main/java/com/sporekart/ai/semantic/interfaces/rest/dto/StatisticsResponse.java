package com.sporekart.ai.semantic.interfaces.rest.dto;

import java.util.Map;

public record StatisticsResponse(
        String indexName,
        Map<String, Double> statistics,
        String recordedAt) {}
