package com.sporekart.ai.pipeline;

public class PipelineException extends RuntimeException {
    private final String requestId;
    private final String pipelineId;
    private final ErrorCode errorCode;

    public enum ErrorCode {
        VALIDATION_FAILED,
        AUTHENTICATION_FAILED,
        AUTHORIZATION_FAILED,
        RATE_LIMITED,
        QUOTA_EXCEEDED,
        PROVIDER_UNAVAILABLE,
        PROVIDER_TIMEOUT,
        PROVIDER_ERROR,
        PROMPT_COMPILATION_FAILED,
        CONTEXT_BUILD_FAILED,
        RETRY_EXHAUSTED,
        TIMEOUT,
        NORMALIZATION_FAILED,
        INTERNAL_ERROR,
        UNKNOWN
    }

    public PipelineException(String requestId, ErrorCode errorCode, String message) {
        super(message);
        this.requestId = requestId;
        this.pipelineId = null;
        this.errorCode = errorCode;
    }

    public PipelineException(String requestId, String pipelineId, ErrorCode errorCode, String message) {
        super(message);
        this.requestId = requestId;
        this.pipelineId = pipelineId;
        this.errorCode = errorCode;
    }

    public PipelineException(String requestId, ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.requestId = requestId;
        this.pipelineId = null;
        this.errorCode = errorCode;
    }

    public String requestId() { return requestId; }
    public String pipelineId() { return pipelineId; }
    public ErrorCode errorCode() { return errorCode; }

    public String toErrorResponse() {
        return "[" + errorCode.name() + "] " + getMessage();
    }
}
