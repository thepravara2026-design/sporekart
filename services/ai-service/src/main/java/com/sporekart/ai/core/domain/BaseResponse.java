package com.sporekart.ai.core.domain;

import java.time.OffsetDateTime;

public abstract class BaseResponse {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final OffsetDateTime timestamp;

    protected BaseResponse(boolean success) {
        this.success = success;
        this.errorCode = null;
        this.errorMessage = null;
        this.timestamp = OffsetDateTime.now();
    }

    protected BaseResponse(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = OffsetDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
