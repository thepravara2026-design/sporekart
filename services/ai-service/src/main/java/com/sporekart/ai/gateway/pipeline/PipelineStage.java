package com.sporekart.ai.gateway.pipeline;

public enum PipelineStage {
    VALIDATION(1, "Request validation"),
    AUTHENTICATION(2, "Identity verification"),
    AUTHORIZATION(3, "Access control"),
    QUOTA_CHECK(4, "Quota verification"),
    RATE_LIMITER(5, "Rate limit enforcement"),
    PROVIDER_SELECTION(6, "Provider resolution"),
    ROUTING(7, "Request routing"),
    EXECUTION(8, "Provider execution"),
    POST_PROCESSING(9, "Response post-processing"),
    AUDIT(10, "Audit logging"),
    METRICS(11, "Metrics collection");

    private final int order;
    private final String description;

    PipelineStage(int order, String description) {
        this.order = order;
        this.description = description;
    }

    public int order() { return order; }
    public String description() { return description; }
}
