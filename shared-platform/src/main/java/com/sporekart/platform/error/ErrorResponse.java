package com.sporekart.platform.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        String message,
        int status,
        String errorCode,
        Instant timestamp,
        String path,
        String traceId,
        List<ValidationException.ValidationError> validationErrors
) {
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private boolean success = false;
        private String message;
        private int status = 500;
        private String errorCode;
        private Instant timestamp = Instant.now();
        private String path;
        private String traceId;
        private List<ValidationException.ValidationError> validationErrors;

        public Builder message(String message) { this.message = message; return this; }
        public Builder status(int status) { this.status = status; return this; }
        public Builder errorCode(String errorCode) { this.errorCode = errorCode; return this; }
        public Builder path(String path) { this.path = path; return this; }
        public Builder traceId(String traceId) { this.traceId = traceId; return this; }
        public Builder validationErrors(List<ValidationException.ValidationError> errors) { this.validationErrors = errors; return this; }

        public ErrorResponse build() {
            return new ErrorResponse(success, message, status, errorCode, timestamp, path, traceId, validationErrors);
        }
    }

    public static ErrorResponse fromException(SporekartException ex, String path, String traceId) {
        return builder()
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .errorCode(ex.getErrorCode().name())
                .path(path)
                .traceId(traceId)
                .build();
    }
}
