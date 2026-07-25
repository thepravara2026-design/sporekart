package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowDecisionEngineTest {
    private WorkflowDecisionEngine decisionEngine;

    @BeforeEach
    void setUp() {
        decisionEngine = new WorkflowDecisionEngine();
    }

    @Test
    void shouldReturnOrderDecisions() {
        var instance = createInstance(WorkflowType.ORDER);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("VALIDATE_INVENTORY"));
        assertTrue(decisions.contains("CHECK_PAYMENT_STATUS"));
    }

    @Test
    void shouldReturnInventoryDecisions() {
        var instance = createInstance(WorkflowType.INVENTORY);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("CHECK_STOCK_THRESHOLD"));
    }

    @Test
    void shouldReturnTrainingDecisions() {
        var instance = createInstance(WorkflowType.TRAINING);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("VERIFY_ELIGIBILITY"));
    }

    @Test
    void shouldReturnVendorDecisions() {
        var instance = createInstance(WorkflowType.VENDOR);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("APPROVE_VENDOR"));
    }

    @Test
    void shouldReturnGrowerDecisions() {
        var instance = createInstance(WorkflowType.GROWER);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("VERIFY_IDENTITY"));
    }

    @Test
    void shouldReturnCustomerDecisions() {
        var instance = createInstance(WorkflowType.CUSTOMER_LIFECYCLE);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("REGISTER_CUSTOMER"));
    }

    @Test
    void shouldReturnMarketingDecisions() {
        var instance = createInstance(WorkflowType.MARKETING);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("DEFINE_CAMPAIGN"));
    }

    @Test
    void shouldReturnExecutiveDecisions() {
        var instance = createInstance(WorkflowType.EXECUTIVE);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("COLLECT_KPIS"));
    }

    @Test
    void shouldReturnAiDecisions() {
        var instance = createInstance(WorkflowType.AI);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("START_TRAINING"));
    }

    @Test
    void shouldReturnAutomationDecisions() {
        var instance = createInstance(WorkflowType.AUTOMATION);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("RUN_HEALTH_CHECKS"));
    }

    @Test
    void shouldIncludeSimulationModeDecision() {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "Test", "system",
            "system", true, true, 3);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("SIMULATION_MODE_ENABLED"));
    }

    @Test
    void shouldIncludeRetryDecision() {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "Test", "system",
            "system", false, false, 3);
        instance = instance.withState(WorkflowState.RETRYING).incrementRetry();
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.stream().anyMatch(d -> d.startsWith("RETRY_ATTEMPT_")));
    }

    @Test
    void shouldIncludeApprovalGateDecision() {
        var instance = createInstance(WorkflowType.ORDER);
        var decisions = decisionEngine.evaluateWorkflow(instance);
        assertTrue(decisions.contains("APPROVAL_GATE_ACTIVE"));
    }

    @Test
    void shouldEvaluateDryRun() {
        var def = WorkflowDefinition.create("Test", "Desc", WorkflowType.ORDER, "Domain", "owner", "1.0.0",
            List.of(new WorkflowStep("Step1", "validation", "desc", List.of(), Map.of(), 1)), Map.of(), Map.of());
        var decisions = decisionEngine.evaluateDryRun(def);
        assertTrue(decisions.contains("DRY_RUN_MODE"));
        assertTrue(decisions.contains("STEP_STEP1_VALIDATION"));
    }

    private WorkflowInstance createInstance(WorkflowType type) {
        return WorkflowInstance.create("def-1", "Test", type, "Test", "system",
            "system", false, true, 3);
    }
}
