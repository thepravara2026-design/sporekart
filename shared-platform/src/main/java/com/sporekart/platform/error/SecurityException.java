package com.sporekart.platform.error;

public class SecurityException extends SporekartException {
    public SecurityException(String message) {
        super(ErrorCode.UNAUTHORIZED, message, message, 401);
    }

    public SecurityException(String message, int httpStatus) {
        super(httpStatus == 403 ? ErrorCode.FORBIDDEN : ErrorCode.UNAUTHORIZED, message, message, httpStatus);
    }
}
