package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.CompanyHealthScore;
import com.sporekart.bi.copilot.domain.VisualizationConfig;

import java.util.List;

public record HealthScoreResponse(
    CompanyHealthScore healthScore,
    List<VisualizationConfig> scoreVisualizations
) {}