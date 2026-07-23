package com.sporekart.bi.copilot.domain;

import java.util.List;
import java.util.Map;

public record VisualizationConfig(
    String vizId,
    String type,
    String title,
    String dataSource,
    Map<String, Object> options,
    List<String> labels,
    List<Double> values,
    List<String> colors,
    Map<String, Object> metadata
) {}
