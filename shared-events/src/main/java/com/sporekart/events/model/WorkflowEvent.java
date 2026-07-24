package com.sporekart.events.model;

public class WorkflowEvent extends AbstractEvent {
    private final String workflowName;
    private final String workflowStep;
    private final String workflowStatus;

    private WorkflowEvent(Builder builder) {
        super(builder);
        this.workflowName = builder.workflowName;
        this.workflowStep = builder.workflowStep;
        this.workflowStatus = builder.workflowStatus;
    }

    public String getWorkflowName() { return workflowName; }
    public String getWorkflowStep() { return workflowStep; }
    public String getWorkflowStatus() { return workflowStatus; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractEvent.Builder<Builder> {
        private String workflowName;
        private String workflowStep;
        private String workflowStatus;

        public Builder workflowName(String workflowName) { this.workflowName = workflowName; return this; }
        public Builder workflowStep(String workflowStep) { this.workflowStep = workflowStep; return this; }
        public Builder workflowStatus(String workflowStatus) { this.workflowStatus = workflowStatus; return this; }

        public WorkflowEvent build() { return new WorkflowEvent(this); }
    }
}
