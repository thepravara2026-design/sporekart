package com.sporekart.ai.gateway.contract.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
    String errorCode,
    String message,
    int statusCode,
    String details,
    List<String> errors,
    String requestId,
    Instant timestamp,
    Map<String, Object> additionalInfo
) {
    public static ErrorResponse of(String errorCode, String message, int statusCode) {
        return new ErrorResponse(errorCode, message, statusCode, null, List.of(), null, Instant.now(), null);
    }
}
