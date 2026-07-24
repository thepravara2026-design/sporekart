package com.sporekart.platform.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditLogger {
    private static final Logger auditLog = LoggerFactory.getLogger("audit");

    public static void log(String action, String resourceType, String resourceId, String userId, String detail) {
        auditLog.info("AUDIT action={} resourceType={} resourceId={} userId={} detail={} correlationId={}",
                action, resourceType, resourceId, userId, detail, LoggingContext.getCorrelationId());
    }

    public static void logCreate(String resourceType, String resourceId, String userId) {
        log("CREATE", resourceType, resourceId, userId, "Created " + resourceType);
    }

    public static void logUpdate(String resourceType, String resourceId, String userId) {
        log("UPDATE", resourceType, resourceId, userId, "Updated " + resourceType);
    }

    public static void logDelete(String resourceType, String resourceId, String userId) {
        log("DELETE", resourceType, resourceId, userId, "Deleted " + resourceType);
    }

    public static void logAccess(String resourceType, String resourceId, String userId) {
        log("ACCESS", resourceType, resourceId, userId, "Accessed " + resourceType);
    }
}
