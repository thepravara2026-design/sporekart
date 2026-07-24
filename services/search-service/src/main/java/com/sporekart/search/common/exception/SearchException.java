package com.sporekart.search.common.exception;

public abstract class SearchException extends RuntimeException {
    protected SearchException(String message) {
        super(message);
    }

    protected SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}