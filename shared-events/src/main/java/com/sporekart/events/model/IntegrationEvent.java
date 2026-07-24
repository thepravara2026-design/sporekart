package com.sporekart.events.model;

public abstract class IntegrationEvent extends AbstractEvent {
    private final String sourceService;
    private final String targetService;

    protected IntegrationEvent(IntegrationBuilder<?> builder) {
        super(builder);
        this.sourceService = builder.sourceService;
        this.targetService = builder.targetService;
    }

    public String getSourceService() { return sourceService; }
    public String getTargetService() { return targetService; }

    public static abstract class IntegrationBuilder<T extends IntegrationBuilder<T>> extends AbstractEvent.Builder<T> {
        private String sourceService;
        private String targetService;

        public T sourceService(String sourceService) { this.sourceService = sourceService; return (T) this; }
        public T targetService(String targetService) { this.targetService = targetService; return (T) this; }
    }
}
