package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.DecisionRecommendation;

import java.util.List;

public record DecisionSupportResponse(
    List<DecisionRecommendation> recommendations,
    String focus,
    String period
) {}