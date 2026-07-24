package com.sporekart.platform.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp,
        String traceId,
        String errorCode,
        Map<String, Object> extensions
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String type = "about:blank";
        private String title;
        private int status = 500;
        private String detail;
        private String instance;
        private Instant timestamp = Instant.now();
        private String traceId;
        private String errorCode;
        private Map<String, Object> extensions;

        public Builder type(String type) { this.type = type; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder status(int status) { this.status = status; return this; }
        public Builder detail(String detail) { this.detail = detail; return this; }
        public Builder instance(String instance) { this.instance = instance; return this; }
        public Builder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }
        public Builder traceId(String traceId) { this.traceId = traceId; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public Builder extensions(Map<String, Object> extensions) { this.extensions = extensions; return this; }

        public ProblemDetails build() {
            return new ProblemDetails(type, title, status, detail, instance, timestamp, traceId, errorCode, extensions);
        }
    }

    public static ProblemDetails fromException(SporekartException ex, String instance, String traceId) {
        return builder()
                .type("https://api.sporekart.com/errors/" + ex.getErrorCode().name().toLowerCase())
                .title(ex.getMessage())
                .status(ex.getHttpStatus())
                .detail(ex.getDetail())
                .instance(instance)
                .traceId(traceId)
                .errorCode(ex.getErrorCode().name())
                .build();
    }
}
