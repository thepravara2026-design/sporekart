package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowDefinition;
import com.sporekart.workflow.domain.model.WorkflowInstance;
import com.sporekart.workflow.domain.model.WorkflowState;

import java.util.ArrayList;
import java.util.List;

public class WorkflowDecisionEngine {

    public List<String> evaluateWorkflow(WorkflowInstance instance) {
        var decisions = new ArrayList<String>();

        if (instance.simulationMode()) {
            decisions.add("SIMULATION_MODE_ENABLED");
        }

        switch (instance.type()) {
            case ORDER -> decisions.addAll(evaluateOrderWorkflow(instance));
            case INVENTORY -> decisions.addAll(evaluateInventoryWorkflow(instance));
            case TRAINING -> decisions.addAll(evaluateTrainingWorkflow(instance));
            case VENDOR -> decisions.addAll(evaluateVendorWorkflow(instance));
            case GROWER -> decisions.addAll(evaluateGrowerWorkflow(instance));
            case CUSTOMER_LIFECYCLE -> decisions.addAll(evaluateCustomerWorkflow(instance));
            case MARKETING -> decisions.addAll(evaluateMarketingWorkflow(instance));
            case EXECUTIVE -> decisions.addAll(evaluateExecutiveWorkflow(instance));
            case AI -> decisions.addAll(evaluateAiWorkflow(instance));
            case AUTOMATION -> decisions.addAll(evaluateAutomationWorkflow(instance));
            default -> decisions.add("STANDARD_EXECUTION");
        }

        if (instance.state() == WorkflowState.RETRYING) {
            decisions.add("RETRY_ATTEMPT_" + instance.retryCount());
        }

        if (instance.approvalRequired()) {
            decisions.add("APPROVAL_GATE_ACTIVE");
        }

        return List.copyOf(decisions);
    }

    public List<String> evaluateDryRun(WorkflowDefinition def) {
        var decisions = new ArrayList<String>();
        decisions.add("DRY_RUN_MODE");
        decisions.add("TYPE_" + def.type().name());
        decisions.add("STEPS_" + def.steps().size() + "_EVALUATED");

        for (var step : def.steps()) {
            decisions.add("STEP_" + step.name().toUpperCase().replace(' ', '_') + "_" + step.type().toUpperCase());
        }

        return List.copyOf(decisions);
    }

    private List<String> evaluateOrderWorkflow(WorkflowInstance instance) {
        return List.of("VALIDATE_INVENTORY", "CHECK_PAYMENT_STATUS", "PROCESS_SHIPMENT", "UPDATE_ORDER_STATUS");
    }

    private List<String> evaluateInventoryWorkflow(WorkflowInstance instance) {
        return List.of("CHECK_STOCK_THRESHOLD", "GENERATE_RESTOCK_RECOMMENDATION", "TRIGGER_REORDER");
    }

    private List<String> evaluateTrainingWorkflow(WorkflowInstance instance) {
        return List.of("VERIFY_ELIGIBILITY", "ASSIGN_BATCH", "ASSIGN_TRAINER", "CONFIRM_ENROLLMENT");
    }

    private List<String> evaluateVendorWorkflow(WorkflowInstance instance) {
        return List.of("VALIDATE_DOCUMENTS", "PERFORM_BACKGROUND_CHECK", "APPROVE_VENDOR", "SETUP_CATALOG");
    }

    private List<String> evaluateGrowerWorkflow(WorkflowInstance instance) {
        return List.of("VERIFY_IDENTITY", "CHECK_CERTIFICATION", "ASSIGN_TRAINING", "GRANT_ACCESS");
    }

    private List<String> evaluateCustomerWorkflow(WorkflowInstance instance) {
        return List.of("REGISTER_CUSTOMER", "VERIFY_EMAIL", "ASSIGN_TIER", "SETUP_PREFERENCES");
    }

    private List<String> evaluateMarketingWorkflow(WorkflowInstance instance) {
        return List.of("DEFINE_CAMPAIGN", "VALIDATE_BUDGET", "SETUP_CHANNELS", "LAUNCH_CAMPAIGN");
    }

    private List<String> evaluateExecutiveWorkflow(WorkflowInstance instance) {
        return List.of("COLLECT_KPIS", "GENERATE_REPORT", "SCHEDULE_REVIEW", "DISTRIBUTE_RESULTS");
    }

    private List<String> evaluateAiWorkflow(WorkflowInstance instance) {
        return List.of("PREPARE_DATA", "VALIDATE_DATA_QUALITY", "START_TRAINING", "EVALUATE_MODEL", "DEPLOY_MODEL");
    }

    private List<String> evaluateAutomationWorkflow(WorkflowInstance instance) {
        return List.of("RUN_HEALTH_CHECKS", "PROCESS_QUEUE", "GENERATE_REPORTS", "SEND_NOTIFICATIONS");
    }
}
