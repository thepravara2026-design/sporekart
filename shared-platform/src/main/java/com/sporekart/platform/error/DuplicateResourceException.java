package com.sporekart.platform.error;

public class DuplicateResourceException extends SporekartException {
    public DuplicateResourceException(String resource, String field, String value) {
        super(ErrorCode.DUPLICATE_RESOURCE,
                resource + " already exists",
                resource + " with " + field + " '" + value + "' already exists.",
                409);
    }

    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_RESOURCE, message, message, 409);
    }
}
