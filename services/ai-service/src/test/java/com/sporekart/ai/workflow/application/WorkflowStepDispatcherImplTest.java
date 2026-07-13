package com.sporekart.ai.workflow.application;

import com.sporekart.ai.workflow.domain.WorkflowStep;
import com.sporekart.ai.workflow.domain.WorkflowStepStatus;
import com.sporekart.ai.workflow.domain.WorkflowStepType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorkflowStepDispatcherImplTest {

    private WorkflowStepDispatcherImpl dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new WorkflowStepDispatcherImpl();
    }

    @Test
    void shouldDispatchAiGatewayStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "AI Gateway",
                WorkflowStepType.AI_GATEWAY, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchPromptStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Prompt",
                WorkflowStepType.PROMPT, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchKnowledgeRetrievalStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Knowledge",
                WorkflowStepType.KNOWLEDGE_RETRIEVAL, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchSemanticSearchStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Search",
                WorkflowStepType.SEMANTIC_SEARCH, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchBusinessServiceStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Business",
                WorkflowStepType.BUSINESS_SERVICE, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchRestApiCallStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "REST API",
                WorkflowStepType.REST_API_CALL, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchNotificationStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Notify",
                WorkflowStepType.NOTIFICATION, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchDelayStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Delay",
                WorkflowStepType.DELAY, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchDecisionStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Decision",
                WorkflowStepType.DECISION, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchConditionalBranchStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Condition",
                WorkflowStepType.CONDITIONAL_BRANCH, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchLoopStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Loop",
                WorkflowStepType.LOOP, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchRetryStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Retry",
                WorkflowStepType.RETRY, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchAuditStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Audit",
                WorkflowStepType.AUDIT, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchLoggingStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Logging",
                WorkflowStepType.LOGGING, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldDispatchCustomStep() {
        var step = new WorkflowStep(UUID.randomUUID(), UUID.randomUUID(), "Custom",
                WorkflowStepType.CUSTOM, 1, "{}", null, false, 5000, 3);

        var status = dispatcher.dispatch(UUID.randomUUID(), step);

        assertEquals(WorkflowStepStatus.COMPLETED, status);
    }

    @Test
    void shouldHandleStepCompletion() {
        var executionId = UUID.randomUUID();
        var stepId = UUID.randomUUID();

        dispatcher.handleStepCompletion(executionId, stepId, "result");
    }

    @Test
    void shouldHandleStepFailure() {
        var executionId = UUID.randomUUID();
        var stepId = UUID.randomUUID();

        dispatcher.handleStepFailure(executionId, stepId, "error");
    }
}
