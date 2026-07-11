package com.sporekart.ai.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class BaseRequest {
    private final String requestId;
    private final String correlationId;
    private final OffsetDateTime timestamp;
    private final String source;

    protected BaseRequest() {
        this.requestId = UUID.randomUUID().toString();
        this.correlationId = null;
        this.timestamp = OffsetDateTime.now();
        this.source = null;
    }

    protected BaseRequest(String correlationId, String source) {
        this.requestId = UUID.randomUUID().toString();
        this.correlationId = correlationId;
        this.timestamp = OffsetDateTime.now();
        this.source = source;
    }

    public String getRequestId() { return requestId; }
    public String getCorrelationId() { return correlationId; }
    public OffsetDateTime getTimestamp() { return timestamp; }
    public String getSource() { return source; }
}
