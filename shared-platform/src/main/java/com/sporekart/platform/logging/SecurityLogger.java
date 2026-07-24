package com.sporekart.platform.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityLogger {
    private static final Logger securityLog = LoggerFactory.getLogger("security");

    public static void logAuthAttempt(String userId, boolean success, String detail) {
        if (success) {
            securityLog.info("AUTH_SUCCESS userId={} detail={} correlationId={}",
                    userId, detail, LoggingContext.getCorrelationId());
        } else {
            securityLog.warn("AUTH_FAILURE userId={} detail={} correlationId={}",
                    userId, detail, LoggingContext.getCorrelationId());
        }
    }

    public static void logAccessDenied(String userId, String resource, String permission) {
        securityLog.warn("ACCESS_DENIED userId={} resource={} permission={} correlationId={}",
                userId, resource, permission, LoggingContext.getCorrelationId());
    }

    public static void logPermissionCheck(String userId, String permission, boolean granted) {
        if (granted) {
            securityLog.debug("PERMISSION_GRANTED userId={} permission={}", userId, permission);
        } else {
            securityLog.warn("PERMISSION_DENIED userId={} permission={}", userId, permission);
        }
    }
}
