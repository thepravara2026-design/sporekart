package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.BusinessForecast;
import com.sporekart.bi.copilot.domain.VisualizationConfig;

import java.util.List;

public record ForecastResponse(
    BusinessForecast forecast,
    List<VisualizationConfig> visualizations
) {}