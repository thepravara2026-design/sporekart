package com.sporekart.events.model;

import java.util.UUID;

public class CorrelationIds {
    private final String correlationId;
    private final String causationId;
    private final String traceId;
    private final String requestId;
    private final String workflowId;
    private final String conversationId;

    private CorrelationIds(Builder builder) {
        this.correlationId = builder.correlationId;
        this.causationId = builder.causationId;
        this.traceId = builder.traceId;
        this.requestId = builder.requestId;
        this.workflowId = builder.workflowId;
        this.conversationId = builder.conversationId;
    }

    public String getCorrelationId() { return correlationId; }
    public String getCausationId() { return causationId; }
    public String getTraceId() { return traceId; }
    public String getRequestId() { return requestId; }
    public String getWorkflowId() { return workflowId; }
    public String getConversationId() { return conversationId; }

    public static Builder builder() {
        return new Builder();
    }

    public static CorrelationIds newId() {
        String id = UUID.randomUUID().toString();
        return builder().correlationId(id).causationId(id).traceId(id).build();
    }

    public static CorrelationIds fromCausation(CorrelationIds parent) {
        return builder()
                .correlationId(parent.getCorrelationId())
                .causationId(parent.getCausationId())
                .traceId(parent.getTraceId())
                .requestId(parent.getRequestId())
                .workflowId(parent.getWorkflowId())
                .conversationId(parent.getConversationId())
                .build();
    }

    public static class Builder {
        private String correlationId;
        private String causationId;
        private String traceId;
        private String requestId;
        private String workflowId;
        private String conversationId;

        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder causationId(String causationId) { this.causationId = causationId; return this; }
        public Builder traceId(String traceId) { this.traceId = traceId; return this; }
        public Builder requestId(String requestId) { this.requestId = requestId; return this; }
        public Builder workflowId(String workflowId) { this.workflowId = workflowId; return this; }
        public Builder conversationId(String conversationId) { this.conversationId = conversationId; return this; }

        public CorrelationIds build() {
            return new CorrelationIds(this);
        }
    }
}
