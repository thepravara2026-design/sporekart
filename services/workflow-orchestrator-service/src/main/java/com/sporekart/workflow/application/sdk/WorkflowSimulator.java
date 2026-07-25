package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.engine.WorkflowSimulationEngine;
import com.sporekart.workflow.domain.model.WorkflowSimulation;

import java.util.Map;

public class WorkflowSimulator {
    private final WorkflowSimulationEngine simulationEngine;

    public WorkflowSimulator(WorkflowSimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
    }

    public WorkflowSimulation simulate(String instanceId) {
        return simulationEngine.simulateWorkflow(instanceId);
    }

    public WorkflowSimulation simulateRollback(String instanceId) {
        return simulationEngine.simulateRollback(instanceId);
    }

    public WorkflowSimulation dryRun(String definitionId, Map<String, Object> context) {
        return simulationEngine.dryRunWorkflow(definitionId, context);
    }
}
