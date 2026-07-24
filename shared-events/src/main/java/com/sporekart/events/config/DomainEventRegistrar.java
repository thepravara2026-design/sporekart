package com.sporekart.events.config;

import com.sporekart.events.registry.EventRegistry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainEventRegistrar {
    private static final Logger log = LoggerFactory.getLogger(DomainEventRegistrar.class);

    private final EventRegistry registry;

    public DomainEventRegistrar(EventRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void registerDomainEvents() {
        // Customer
        registry.register("customer.registered", "Customer registered", "customer");
        registry.register("customer.updated", "Customer updated", "customer");

        // Order
        registry.register("order.created", "Order created", "order");
        registry.register("order.paid", "Order paid", "order");
        registry.register("order.cancelled", "Order cancelled", "order");

        // Payment
        registry.register("payment.succeeded", "Payment succeeded", "payment");
        registry.register("payment.failed", "Payment failed", "payment");

        // Inventory
        registry.register("inventory.reserved", "Inventory reserved", "inventory");
        registry.register("inventory.released", "Inventory released", "inventory");
        registry.register("inventory.low", "Inventory low", "inventory");
        registry.register("inventory.updated", "Inventory updated", "inventory");

        // Cart
        registry.register("cart.created", "Cart created", "cart");
        registry.register("cart.abandoned", "Cart abandoned", "cart");
        registry.register("cart.checked-out", "Cart checked out", "cart");

        // Catalog
        registry.register("product.created", "Product created", "catalog");
        registry.register("product.updated", "Product updated", "catalog");
        registry.register("product.deleted", "Product deleted", "catalog");

        // Content
        registry.register("review.submitted", "Review submitted", "content");
        registry.register("review.approved", "Review approved", "content");
        registry.register("review.rejected", "Review rejected", "content");

        // Fulfillment
        registry.register("shipment.created", "Shipment created", "fulfillment");
        registry.register("shipment.delivered", "Shipment delivered", "fulfillment");
        registry.register("shipment.delayed", "Shipment delayed", "fulfillment");

        // Notification
        registry.register("notification.sent", "Notification sent", "notification");

        // Identity
        registry.register("user.registered", "User registered", "identity");
        registry.register("user.logged-in", "User logged in", "identity");
        registry.register("password.changed", "Password changed", "identity");

        // Training
        registry.register("training.registered", "Training registered", "training");
        registry.register("training.completed", "Training completed", "training");
        registry.register("certificate.generated", "Certificate generated", "training");

        // Analytics
        registry.register("report.generated", "Report generated", "analytics");

        // Admin
        registry.register("admin.action.performed", "Admin action performed", "admin");

        // Risk
        registry.register("risk.assessment.created", "Risk assessment created", "risk");
        registry.register("fraud.detected", "Fraud detected", "risk");

        // Search
        registry.register("document.indexed", "Document indexed", "search");

        // Support
        registry.register("ticket.created", "Support ticket created", "support");
        registry.register("ticket.resolved", "Support ticket resolved", "support");

        // AI
        registry.register("ai.conversation.started", "AI conversation started", "ai");
        registry.register("ai.conversation.completed", "AI conversation completed", "ai");
        registry.register("ai.action.executed", "AI action executed", "ai");

        // Knowledge
        registry.register("knowledge.created", "Knowledge entry created", "knowledge");
        registry.register("knowledge.updated", "Knowledge entry updated", "knowledge");

        // Prompt
        registry.register("prompt.published", "Prompt published", "prompt");
        registry.register("prompt.updated", "Prompt updated", "prompt");

        // Memory
        registry.register("memory.updated", "Memory updated", "memory");

        // Copilot
        registry.register("copilot.action.executed", "Copilot action executed", "copilot");
        registry.register("copilot.suggestion.generated", "Copilot suggestion generated", "copilot");

        // Plugin
        registry.register("plugin.installed", "Plugin installed", "plugin");
        registry.register("plugin.uninstalled", "Plugin uninstalled", "plugin");

        // Marketing
        registry.register("coupon.applied", "Coupon applied", "marketing");
        registry.register("campaign.started", "Campaign started", "marketing");

        log.info("Registered {} domain events", registry.count());
    }
}
