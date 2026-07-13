package com.sporekart.ai.risk.interfaces.rest.dto;

public record ThresholdUpdateDto(
    String level,
    double minScore,
    double maxScore,
    String action
) {}
