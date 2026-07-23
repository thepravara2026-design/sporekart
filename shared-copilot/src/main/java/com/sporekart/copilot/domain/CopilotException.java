package com.sporekart.copilot.domain;

import java.util.Objects;

public class CopilotException extends RuntimeException {

    private final String errorCode;

    public CopilotException(String errorCode, String message) {
        super(message);
        Objects.requireNonNull(errorCode, "errorCode must not be null");
        if (errorCode.isBlank()) throw new IllegalArgumentException("errorCode must not be blank");
        this.errorCode = errorCode;
    }

    public CopilotException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        Objects.requireNonNull(errorCode, "errorCode must not be null");
        if (errorCode.isBlank()) throw new IllegalArgumentException("errorCode must not be blank");
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "CopilotException{errorCode='" + errorCode + "', message='" + getMessage() + "'}";
    }
}
