package com.sporekart.platform.error;

public class NotFoundException extends SporekartException {
    public NotFoundException(String resource, String id) {
        super(ErrorCode.NOT_FOUND,
                resource + " not found: " + id,
                "The requested " + resource.toLowerCase() + " with identifier '" + id + "' does not exist.",
                404);
    }

    public NotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message, message, 404);
    }
}
