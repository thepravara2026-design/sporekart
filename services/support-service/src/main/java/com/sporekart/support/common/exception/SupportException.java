package com.sporekart.support.common.exception;

public abstract class SupportException extends RuntimeException {
    protected SupportException(String message) {
        super(message);
    }
}