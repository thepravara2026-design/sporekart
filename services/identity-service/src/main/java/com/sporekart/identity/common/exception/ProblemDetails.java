package com.sporekart.identity.common.exception;

import java.time.OffsetDateTime;
import java.util.Map;

public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        OffsetDateTime timestamp,
        Map<String, Object> extensions) {
}
