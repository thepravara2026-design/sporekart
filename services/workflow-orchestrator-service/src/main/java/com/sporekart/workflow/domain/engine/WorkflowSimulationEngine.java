package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.time.Instant;
import java.util.*;

public class WorkflowSimulationEngine {
    private final WorkflowRepositoryPort repository;
    private final WorkflowDecisionEngine decisionEngine;
    private final Random random = new Random();

    public WorkflowSimulationEngine(WorkflowRepositoryPort repository, WorkflowDecisionEngine decisionEngine) {
        this.repository = repository;
        this.decisionEngine = decisionEngine;
    }

    public WorkflowSimulation simulateWorkflow(String instanceId) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var decisions = decisionEngine.evaluateWorkflow(instance);
        var actions = executeMockActions(instance, decisions);
        var success = !actions.isEmpty() && actions.stream().noneMatch(a -> "FAILED".equals(a.status()));
        var errors = actions.stream().filter(a -> "FAILED".equals(a.status())).map(a -> a.name() + ": " + a.description()).toList();

        var result = new HashMap<String, Object>();
        result.put("workflowName", instance.name());
        result.put("type", instance.type().name());
        result.put("simulationMode", instance.simulationMode());
        result.put("totalActions", actions.size());
        result.put("successfulActions", actions.stream().filter(a -> "EXECUTED".equals(a.status())).count());
        result.put("failedActions", actions.stream().filter(a -> "FAILED".equals(a.status())).count());
        result.put("decisions", decisions);
        result.put("completedAt", Instant.now().toString());

        var simulation = WorkflowSimulation.create(
            instanceId, instance.name(), instance.type(), success,
            instance.approvalRequired(), !instance.approvalRequired(),
            actions, decisions, errors, result
        );

        repository.saveAudit(WorkflowAudit.create(instanceId, "SIMULATION_EXECUTED", "system",
            "Simulation " + (success ? "succeeded" : "failed") + " for " + instance.name(),
            success ? "SUCCESS" : "FAILURE", result));

        return repository.saveSimulation(simulation);
    }

    private List<MockAction> executeMockActions(WorkflowInstance instance, List<String> decisions) {
        var actions = new ArrayList<MockAction>();
        var def = repository.findDefinitionById(instance.definitionId());

        if (def.isEmpty()) return List.of();

        for (var step : def.get().steps()) {
            var name = step.name();
            var stepType = step.type();

            var input = new HashMap<String, Object>();
            input.put("workflowId", instance.id());
            input.put("stepName", name);
            input.put("stepType", stepType);
            input.put("simulationMode", instance.simulationMode());
            input.put("decisions", decisions);

            if ("execution".equals(stepType) || "completion".equals(stepType)) {
                if (random.nextDouble() < 0.9) {
                    actions.add(MockAction.create(name, stepType, "Executing " + name, input));
                } else {
                    actions.add(MockAction.failed(name, stepType, "Failed to execute " + name, input));
                }
            } else {
                actions.add(MockAction.create(name, stepType, "Processing " + name, input));
            }
        }

        return List.copyOf(actions);
    }

    public WorkflowSimulation simulateRollback(String instanceId) {
        var instance = repository.findInstanceById(instanceId)
            .orElseThrow(() -> new NoSuchElementException("Instance not found: " + instanceId));

        var rollbackDecisions = List.of("ROLLBACK_INITIATED", "REVERSING_ACTIONS", "RESTORING_STATE");
        var actions = new ArrayList<MockAction>();

        actions.add(MockAction.create("Rollback Initiated", "rollback", "Initiating rollback for " + instance.name(), Map.of("instanceId", instanceId)));
        actions.add(MockAction.create("Reversing Actions", "rollback", "Reversing all executed actions", Map.of("instanceId", instanceId)));
        actions.add(MockAction.create("Restoring State", "rollback", "Restoring pre-workflow state", Map.of("instanceId", instanceId)));

        var result = new HashMap<String, Object>();
        result.put("workflowName", instance.name());
        result.put("rollbackStatus", "COMPLETED");
        result.put("actionsRolledBack", actions.size());
        result.put("completedAt", Instant.now().toString());

        var simulation = WorkflowSimulation.create(
            instanceId, instance.name(), instance.type(), true,
            false, true, actions, rollbackDecisions, List.of(), result
        );

        repository.saveAudit(WorkflowAudit.create(instanceId, "ROLLBACK_SIMULATED", "system",
            "Rollback simulation completed for " + instance.name(), "SUCCESS", result));

        return repository.saveSimulation(simulation);
    }

    public WorkflowSimulation dryRunWorkflow(String definitionId, Map<String, Object> context) {
        var def = repository.findDefinitionById(definitionId)
            .orElseThrow(() -> new NoSuchElementException("Definition not found: " + definitionId));

        var decisions = decisionEngine.evaluateDryRun(def);
        var actions = new ArrayList<MockAction>();

        for (var step : def.steps()) {
            var input = new HashMap<String, Object>();
            input.put("stepName", step.name());
            input.put("stepType", step.type());
            input.put("dryRun", true);
            input.putAll(context);

            if ("execution".equals(step.type()) || "completion".equals(step.type())) {
                actions.add(MockAction.create(step.name(), step.type(), "DRY RUN: " + step.description(), input));
            } else {
                actions.add(MockAction.create(step.name(), step.type(), "DRY RUN: " + step.description(), input));
            }
        }

        var result = new HashMap<String, Object>();
        result.put("workflowName", def.name());
        result.put("type", def.type().name());
        result.put("dryRun", true);
        result.put("totalSteps", def.steps().size());
        result.put("actionsSimulated", actions.size());
        result.put("decisions", decisions);
        result.put("completedAt", Instant.now().toString());

        var simulation = WorkflowSimulation.create(
            "dry-run-" + definitionId, def.name(), def.type(), true,
            false, true, actions, decisions, List.of(), result
        );

        repository.saveAudit(WorkflowAudit.create(simulation.id(), "DRY_RUN", "system",
            "Dry run completed for " + def.name(), "SUCCESS", result));

        return repository.saveSimulation(simulation);
    }
}
