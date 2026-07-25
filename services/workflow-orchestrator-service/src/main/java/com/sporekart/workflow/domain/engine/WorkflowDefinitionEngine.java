package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkflowDefinitionEngine {
    private final WorkflowRepositoryPort repository;

    public WorkflowDefinitionEngine(WorkflowRepositoryPort repository) {
        this.repository = repository;
    }

    public List<WorkflowDefinition> generateAllDefinitions() {
        var defs = new ArrayList<>(generateOrderDefinitions());
        defs.addAll(generateInventoryDefinitions());
        defs.addAll(generateTrainingDefinitions());
        defs.addAll(generateVendorDefinitions());
        defs.addAll(generateGrowerDefinitions());
        defs.addAll(generateCustomerDefinitions());
        defs.addAll(generateMarketingDefinitions());
        defs.addAll(generateExecutiveDefinitions());
        defs.addAll(generateAiDefinitions());
        defs.addAll(generateAutomationDefinitions());
        return List.copyOf(defs);
    }

    private List<WorkflowDefinition> generateOrderDefinitions() {
        return List.of(
            definition("Order Processing", "Full order lifecycle from placement to delivery", WorkflowType.ORDER, "Orders", "system", "1.0.0", List.of(
                step("Validate Order", "validation", "Validate order details", 1),
                step("Check Inventory", "validation", "Verify inventory availability", 2, "Validate Order"),
                step("Process Payment", "execution", "Process payment verification", 3, "Check Inventory"),
                step("Initiate Packaging", "execution", "Start packaging process", 4, "Process Payment"),
                step("Mark Shipment Ready", "execution", "Mark order ready for shipment", 5, "Initiate Packaging"),
                step("Confirm Delivery", "completion", "Confirm delivery completion", 6, "Mark Shipment Ready")
            )),
            definition("Order Return", "Handle customer order returns", WorkflowType.ORDER, "Orders", "system", "1.0.0", List.of(
                step("Validate Return", "validation", "Validate return eligibility", 1),
                step("Approve Return", "approval", "Manager approval for return", 2, "Validate Return"),
                step("Process Refund", "execution", "Process refund to customer", 3, "Approve Return"),
                step("Update Inventory", "execution", "Return items to inventory", 4, "Process Refund")
            ))
        );
    }

    private List<WorkflowDefinition> generateInventoryDefinitions() {
        return List.of(
            definition("Inventory Restock", "Automated inventory restock workflow", WorkflowType.INVENTORY, "Inventory", "system", "1.0.0", List.of(
                step("Check Stock Levels", "validation", "Check current stock levels", 1),
                step("Generate Reorder", "decision", "Generate reorder recommendation", 2, "Check Stock Levels"),
                step("Approve Purchase", "approval", "Approve purchase request", 3, "Generate Reorder"),
                step("Mock Purchase Order", "execution", "Create simulated purchase order", 4, "Approve Purchase"),
                step("Update Inventory", "execution", "Update inventory records", 5, "Mock Purchase Order")
            )),
            definition("Low Stock Alert", "Handle low stock alerts automatically", WorkflowType.INVENTORY, "Inventory", "system", "1.0.0", List.of(
                step("Detect Low Stock", "validation", "Detect items below threshold", 1),
                step("Generate Alert", "decision", "Generate low stock alert", 2, "Detect Low Stock"),
                step("Recommend Restock", "decision", "Recommend restock quantity", 3, "Generate Alert"),
                step("Trigger Approval", "approval", "Request restock approval", 4, "Recommend Restock"),
                step("Execute Restock", "execution", "Execute mock restock", 5, "Trigger Approval")
            ))
        );
    }

    private List<WorkflowDefinition> generateTrainingDefinitions() {
        return List.of(
            definition("Training Registration", "Handle training registration workflow", WorkflowType.TRAINING, "Training", "system", "1.0.0", List.of(
                step("Check Eligibility", "validation", "Check trainee eligibility", 1),
                step("Assign Batch", "execution", "Assign to training batch", 2, "Check Eligibility"),
                step("Assign Trainer", "execution", "Assign trainer to batch", 3, "Assign Batch"),
                step("Confirm Enrollment", "completion", "Confirm enrollment completion", 4, "Assign Trainer")
            )),
            definition("Training Completion", "Process training completion and certification", WorkflowType.TRAINING, "Training", "system", "1.0.0", List.of(
                step("Evaluate Results", "validation", "Evaluate training results", 1),
                step("Generate Certificate", "execution", "Generate completion certificate", 2, "Evaluate Results"),
                step("Update Profile", "execution", "Update user profile with certification", 3, "Generate Certificate"),
                step("Notify Stakeholders", "completion", "Notify stakeholders of completion", 4, "Update Profile")
            ))
        );
    }

    private List<WorkflowDefinition> generateVendorDefinitions() {
        return List.of(
            definition("Vendor Onboarding", "Complete vendor onboarding workflow", WorkflowType.VENDOR, "Vendors", "system", "1.0.0", List.of(
                step("Validate Documents", "validation", "Validate vendor documents", 1),
                step("Approve Vendor", "approval", "Approve vendor application", 2, "Validate Documents"),
                step("Setup Catalog", "execution", "Setup vendor catalog", 3, "Approve Vendor"),
                step("Activate Account", "execution", "Activate vendor account", 4, "Setup Catalog")
            ))
        );
    }

    private List<WorkflowDefinition> generateGrowerDefinitions() {
        return List.of(
            definition("Grower Onboarding", "Complete grower onboarding workflow", WorkflowType.GROWER, "Growers", "system", "1.0.0", List.of(
                step("Verify Identity", "validation", "Verify grower identity", 1),
                step("Assign Training", "execution", "Assign mandatory training", 2, "Verify Identity"),
                step("Grant Marketplace Access", "execution", "Grant marketplace access", 3, "Assign Training"),
                step("Activate Profile", "completion", "Activate grower profile", 4, "Grant Marketplace Access")
            ))
        );
    }

    private List<WorkflowDefinition> generateCustomerDefinitions() {
        return List.of(
            definition("Customer Lifecycle", "Manage customer lifecycle journey", WorkflowType.CUSTOMER_LIFECYCLE, "Customers", "system", "1.0.0", List.of(
                step("Register Customer", "validation", "Register new customer", 1),
                step("Verify Email", "validation", "Verify customer email", 2, "Register Customer"),
                step("Assign Tier", "decision", "Assign customer tier", 3, "Verify Email"),
                step("Setup Preferences", "execution", "Setup customer preferences", 4, "Assign Tier"),
                step("Send Welcome", "completion", "Send welcome package", 5, "Setup Preferences")
            )),
            definition("Customer Support", "Handle customer support tickets", WorkflowType.CUSTOMER_LIFECYCLE, "Customers", "system", "1.0.0", List.of(
                step("Validate Ticket", "validation", "Validate support ticket", 1),
                step("Assign Agent", "execution", "Assign support agent", 2, "Validate Ticket"),
                step("Resolve Issue", "execution", "Resolve customer issue", 3, "Assign Agent"),
                step("Confirm Resolution", "completion", "Confirm resolution with customer", 4, "Resolve Issue")
            ))
        );
    }

    private List<WorkflowDefinition> generateMarketingDefinitions() {
        return List.of(
            definition("Marketing Campaign", "Execute marketing campaign workflow", WorkflowType.MARKETING, "Marketing", "system", "1.0.0", List.of(
                step("Define Campaign", "validation", "Define campaign parameters", 1),
                step("Approve Campaign", "approval", "Approve campaign budget", 2, "Define Campaign"),
                step("Setup Channels", "execution", "Setup marketing channels", 3, "Approve Campaign"),
                step("Launch Campaign", "execution", "Launch campaign execution", 4, "Setup Channels"),
                step("Monitor Results", "completion", "Monitor campaign performance", 5, "Launch Campaign")
            ))
        );
    }

    private List<WorkflowDefinition> generateExecutiveDefinitions() {
        return List.of(
            definition("Executive Review", "Quarterly executive review workflow", WorkflowType.EXECUTIVE, "Executive", "system", "1.0.0", List.of(
                step("Collect KPIs", "validation", "Collect KPI data", 1),
                step("Generate Report", "decision", "Generate executive report", 2, "Collect KPIs"),
                step("Review Report", "approval", "Executive review of report", 3, "Generate Report"),
                step("Approve Actions", "approval", "Approve action items", 4, "Review Report"),
                step("Distribute Results", "completion", "Distribute review results", 5, "Approve Actions")
            ))
        );
    }

    private List<WorkflowDefinition> generateAiDefinitions() {
        return List.of(
            definition("AI Model Training", "Orchestrate AI model training pipeline", WorkflowType.AI, "AI Platform", "system", "1.0.0", List.of(
                step("Prepare Data", "validation", "Prepare training data", 1),
                step("Validate Data", "validation", "Validate data quality", 2, "Prepare Data"),
                step("Start Training", "execution", "Start model training", 3, "Validate Data"),
                step("Evaluate Model", "validation", "Evaluate model performance", 4, "Start Training"),
                step("Deploy Model", "execution", "Deploy trained model", 5, "Evaluate Model")
            ))
        );
    }

    private List<WorkflowDefinition> generateAutomationDefinitions() {
        return List.of(
            definition("Daily Automation", "Daily automated business processes", WorkflowType.AUTOMATION, "Automation Platform", "system", "1.0.0", List.of(
                step("Run Health Checks", "validation", "Run system health checks", 1),
                step("Process Queue", "execution", "Process pending queue items", 2, "Run Health Checks"),
                step("Generate Reports", "execution", "Generate daily reports", 3, "Process Queue"),
                step("Send Notifications", "execution", "Send pending notifications", 4, "Generate Reports"),
                step("Log Completion", "completion", "Log automation completion", 5, "Send Notifications")
            ))
        );
    }

    private WorkflowStep step(String name, String type, String description, int order, String... dependsOn) {
        return new WorkflowStep(name, type, description, List.of(dependsOn), Map.of(), order);
    }

    private WorkflowDefinition definition(String name, String description, WorkflowType type,
                                          String domain, String owner, String version, List<WorkflowStep> steps) {
        var def = WorkflowDefinition.create(name, description, type, domain, owner, version, steps, Map.of(), Map.of());
        return repository.saveDefinition(def);
    }
}
