package com.sporekart.operations.copilot.domain;

import java.time.LocalDate;

public record OperationalKPI(
    String kpiId,
    String name,
    String category,
    double currentValue,
    double targetValue,
    double previousValue,
    String unit,
    KpiTrend trend,
    LocalDate date
) {
    public enum KpiTrend {
        IMPROVING, DECLINING, STABLE, CRITICAL
    }
}
