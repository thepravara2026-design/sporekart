package com.sporekart.grower.copilot.domain;

import java.util.List;

public record CultivationStage(
    String stageId,
    String stageName,
    int stageOrder,
    String description,
    int durationDays,
    double optimalTempCelsius,
    double optimalHumidityPercent,
    String lightRequirement,
    double co2Ppm,
    String ventilationRequirement,
    List<String> commonIssues,
    List<String> checkpoints
) {}
