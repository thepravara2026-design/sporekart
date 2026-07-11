package com.sporekart.ai.core.application.exception;

public class AiPlatformException extends AiCoreException {
    public AiPlatformException(String message) {
        super(message);
    }

    public AiPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
