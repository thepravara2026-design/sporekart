package com.sporekart.ai.memory.application;

public class MemoryException extends RuntimeException {
    public MemoryException(String message) {
        super(message);
    }

    public MemoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
