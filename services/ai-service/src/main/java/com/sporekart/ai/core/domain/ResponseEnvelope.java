package com.sporekart.ai.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseEnvelope<T>(
        boolean success,
        T data,
        String errorCode,
        String errorMessage,
        OffsetDateTime timestamp,
        String correlationId,
        Map<String, Object> metadata) {
    public static <T> ResponseEnvelope<T> ok(T data) {
        return new ResponseEnvelope<>(true, data, null, null, OffsetDateTime.now(), null, Map.of());
    }

    public static <T> ResponseEnvelope<T> ok(T data, String correlationId) {
        return new ResponseEnvelope<>(true, data, null, null, OffsetDateTime.now(), correlationId, Map.of());
    }

    public static <T> ResponseEnvelope<T> error(String errorCode, String errorMessage) {
        return new ResponseEnvelope<>(false, null, errorCode, errorMessage, OffsetDateTime.now(), null, Map.of());
    }

    public static <T> ResponseEnvelope<T> error(AiErrorCode errorCode) {
        return new ResponseEnvelope<>(false, null, errorCode.getCode(), errorCode.getMessage(),
                OffsetDateTime.now(), null, Map.of());
    }

    public static <T> ResponseEnvelope<T> error(AiErrorCode errorCode, String correlationId) {
        return new ResponseEnvelope<>(false, null, errorCode.getCode(), errorCode.getMessage(),
                OffsetDateTime.now(), correlationId, Map.of());
    }
}
