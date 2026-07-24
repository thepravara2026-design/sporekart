package com.sporekart.platform.observability.tracing;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TraceContext {

    private static final ThreadLocal<TraceContext> CONTEXT = ThreadLocal.withInitial(TraceContext::new);

    private String traceId;
    private String spanId;
    private String parentSpanId;
    private String correlationId;
    private String requestId;
    private String workspaceId;
    private String userId;
    private String serviceName;
    private final Map<String, String> baggage = new HashMap<>();

    private TraceContext() {
        this.traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.correlationId = UUID.randomUUID().toString();
        this.requestId = UUID.randomUUID().toString();
    }

    public static TraceContext get() {
        return CONTEXT.get();
    }

    public static void set(TraceContext context) {
        CONTEXT.set(context);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static TraceContext create(String serviceName) {
        TraceContext ctx = new TraceContext();
        ctx.serviceName = serviceName;
        CONTEXT.set(ctx);
        return ctx;
    }

    public static TraceContext createFromHeaders(Map<String, String> headers) {
        TraceContext ctx = new TraceContext();
        ctx.traceId = headers.getOrDefault("X-Trace-Id", ctx.traceId);
        ctx.spanId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        ctx.parentSpanId = headers.get("X-Span-Id");
        ctx.correlationId = headers.getOrDefault("X-Correlation-Id", ctx.correlationId);
        ctx.requestId = headers.getOrDefault("X-Request-Id", ctx.requestId);
        ctx.workspaceId = headers.get("X-Workspace-Id");
        ctx.userId = headers.get("X-User-Id");
        CONTEXT.set(ctx);
        return ctx;
    }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public String getSpanId() { return spanId; }
    public void setSpanId(String spanId) { this.spanId = spanId; }

    public String getParentSpanId() { return parentSpanId; }
    public void setParentSpanId(String parentSpanId) { this.parentSpanId = parentSpanId; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public void addBaggage(String key, String value) { baggage.put(key, value); }
    public String getBaggage(String key) { return baggage.get(key); }
    public Map<String, String> getBaggage() { return new HashMap<>(baggage); }

    public Map<String, String> toHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Trace-Id", traceId);
        headers.put("X-Span-Id", spanId);
        headers.put("X-Correlation-Id", correlationId);
        headers.put("X-Request-Id", requestId);
        if (parentSpanId != null) headers.put("X-Parent-Span-Id", parentSpanId);
        if (workspaceId != null) headers.put("X-Workspace-Id", workspaceId);
        if (userId != null) headers.put("X-User-Id", userId);
        return headers;
    }

    public Map<String, String> toLoggingContext() {
        Map<String, String> ctx = new HashMap<>();
        ctx.put("traceId", traceId);
        ctx.put("spanId", spanId);
        ctx.put("correlationId", correlationId);
        ctx.put("requestId", requestId);
        if (parentSpanId != null) ctx.put("parentSpanId", parentSpanId);
        if (workspaceId != null) ctx.put("workspaceId", workspaceId);
        if (userId != null) ctx.put("userId", userId);
        if (serviceName != null) ctx.put("service", serviceName);
        return ctx;
    }
}
