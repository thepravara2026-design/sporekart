package com.sporekart.platform.error;

public abstract class SporekartException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;
    private final int httpStatus;

    protected SporekartException(ErrorCode errorCode, String message, String detail, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    protected SporekartException(ErrorCode errorCode, String message, String detail, int httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.detail = detail;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public String getDetail() { return detail; }
    public int getHttpStatus() { return httpStatus; }
}
