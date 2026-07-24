package com.sporekart.platform.error;

public class BusinessException extends SporekartException {
    public BusinessException(String message) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message, message, 422);
    }

    public BusinessException(String message, String detail) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message, detail, 422);
    }
}
