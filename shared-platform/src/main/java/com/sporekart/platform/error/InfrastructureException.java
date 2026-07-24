package com.sporekart.platform.error;

public class InfrastructureException extends SporekartException {
    public InfrastructureException(String message) {
        super(ErrorCode.INTERNAL_ERROR, message, message, 500);
    }

    public InfrastructureException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_ERROR, message, message, 500, cause);
    }

    public InfrastructureException(String message, ErrorCode errorCode, int httpStatus) {
        super(errorCode, message, message, httpStatus);
    }
}
