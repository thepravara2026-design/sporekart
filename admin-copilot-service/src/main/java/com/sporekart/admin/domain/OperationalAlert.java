package com.sporekart.admin.domain;

import java.time.OffsetDateTime;

public record OperationalAlert(
    String id,
    AlertType type,
    String title,
    String description,
    String metric,
    Double threshold,
    Double currentValue,
    OffsetDateTime timestamp,
    String suggestedAction
) {

    public enum AlertType {
        INFO, WARNING, CRITICAL
    }
}
