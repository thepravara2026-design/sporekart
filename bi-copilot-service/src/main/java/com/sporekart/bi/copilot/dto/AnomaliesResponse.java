package com.sporekart.bi.copilot.dto;

import java.util.List;

import com.sporekart.bi.copilot.domain.AnomalyAlert;

public record AnomaliesResponse(
    List<AnomalyAlert> anomalies,
    int total,
    int critical,
    int unresolved,
    String period
) {}
