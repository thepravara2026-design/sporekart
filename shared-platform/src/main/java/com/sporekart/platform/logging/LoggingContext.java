package com.sporekart.platform.logging;

import org.slf4j.MDC;

import java.util.UUID;

public class LoggingContext {
    private static final String CORRELATION_ID = "correlationId";
    private static final String TRACE_ID = "traceId";
    private static final String REQUEST_ID = "requestId";
    private static final String WORKSPACE_ID = "workspaceId";
    private static final String USER_ID = "userId";
    private static final String SERVICE_NAME = "serviceName";

    private LoggingContext() {}

    public static void initialize() {
        MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        MDC.put(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
    }

    public static void setCorrelationId(String correlationId) {
        if (correlationId != null) {
            MDC.put(CORRELATION_ID, correlationId);
        }
    }

    public static void setTraceId(String traceId) {
        if (traceId != null) {
            MDC.put(TRACE_ID, traceId);
        }
    }

    public static void setWorkspaceId(String workspaceId) {
        if (workspaceId != null) {
            MDC.put(WORKSPACE_ID, workspaceId);
        }
    }

    public static void setUserId(String userId) {
        if (userId != null) {
            MDC.put(USER_ID, userId);
        }
    }

    public static void setServiceName(String serviceName) {
        if (serviceName != null) {
            MDC.put(SERVICE_NAME, serviceName);
        }
    }

    public static String getCorrelationId() { return MDC.get(CORRELATION_ID); }
    public static String getTraceId() { return MDC.get(TRACE_ID); }
    public static String getRequestId() { return MDC.get(REQUEST_ID); }
    public static String getWorkspaceId() { return MDC.get(WORKSPACE_ID); }
    public static String getUserId() { return MDC.get(USER_ID); }

    public static void clear() {
        MDC.clear();
    }
}
