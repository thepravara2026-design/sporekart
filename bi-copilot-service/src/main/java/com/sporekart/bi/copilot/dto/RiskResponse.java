package com.sporekart.bi.copilot.dto;

import com.sporekart.bi.copilot.domain.RiskAlert;

import java.util.List;

public record RiskResponse(
    List<RiskAlert> risks,
    int total,
    int critical,
    int high,
    int open,
    String period
) {}