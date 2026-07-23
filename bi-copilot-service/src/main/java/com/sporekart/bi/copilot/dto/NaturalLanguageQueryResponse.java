package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.BusinessInsight;
import com.sporekart.bi.copilot.domain.VisualizationConfig;

import java.util.List;
import java.util.Map;

public record NaturalLanguageQueryResponse(
    String intent,
    String explanation,
    Map<String, Object> data,
    VisualizationConfig visualization,
    List<BusinessInsight> insights,
    String suggestedFollowUp
) {}