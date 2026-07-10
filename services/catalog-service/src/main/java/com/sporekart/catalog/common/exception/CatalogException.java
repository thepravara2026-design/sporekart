package com.sporekart.catalog.common.exception;

public abstract class CatalogException extends RuntimeException {
    protected CatalogException(String message) {
        super(message);
    }
}
