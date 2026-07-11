package com.sporekart.ai.common.exception;

public class OfflineSyncException extends RuntimeException {
    public OfflineSyncException(String message) {
        super(message);
    }

    public OfflineSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
