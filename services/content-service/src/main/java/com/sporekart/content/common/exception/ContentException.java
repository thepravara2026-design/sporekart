package com.sporekart.content.common.exception;

public abstract class ContentException extends RuntimeException {

    protected ContentException(String message) {
        super(message);
    }

    protected ContentException(String message, Throwable cause) {
        super(message, cause);
    }
}
