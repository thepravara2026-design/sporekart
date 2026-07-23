package com.sporekart.bi.copilot.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record KpiResponse(
    List<KpiEntry> kpis,
    String period,
    OffsetDateTime timestamp
) {
    public record KpiEntry(
        String id,
        String name,
        double value,
        double previousValue,
        double changePercent,
        String trend,
        String unit,
        String status
    ) {}
}