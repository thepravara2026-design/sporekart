package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.domain.model.WorkflowType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowBuilderTest {

    @Test
    void shouldBuildWorkflowDefinition() {
        var def = new WorkflowBuilder("Test Workflow", "Test description", WorkflowType.ORDER, "Domain", "owner")
            .addStep("Validate", "validation", "Validate input", 1)
            .addStep("Execute", "execution", "Execute action", 2, "Validate")
            .addConfig("timeout", 30)
            .addMetadata("author", "test")
            .build();

        assertEquals("Test Workflow", def.name());
        assertFalse(def.steps().isEmpty());
        assertEquals(2, def.steps().size());
    }

    @Test
    void shouldBuildWithStepsInOrder() {
        var def = new WorkflowBuilder("Test", "Desc", WorkflowType.INVENTORY, "Inv", "owner")
            .addStep("Step 1", "validation", "First", 1)
            .addStep("Step 2", "execution", "Second", 2, "Step 1")
            .addStep("Step 3", "completion", "Third", 3, "Step 2")
            .build();

        assertEquals(3, def.steps().size());
        assertEquals("Step 1", def.steps().get(0).name());
        assertEquals("Step 3", def.steps().get(2).name());
    }
}
