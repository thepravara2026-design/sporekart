package com.sporekart.ai.risk.interfaces.rest.dto;

public record ThresholdDto(
    String level,
    double minScore,
    double maxScore,
    String action
) {}
