package com.sporekart.gateway.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    String traceId,
    String requestId,
    Instant timestamp,
    String errorCode
) {
    public static ProblemDetails from(HttpStatus status, String errorCode, String detail, String path) {
        return new ProblemDetails(
            "about:blank",
            status.getReasonPhrase(),
            status.value(),
            detail,
            path,
            null,
            null,
            Instant.now(),
            errorCode
        );
    }
}
