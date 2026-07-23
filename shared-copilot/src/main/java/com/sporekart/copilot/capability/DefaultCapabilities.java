package com.sporekart.copilot.capability;

import com.sporekart.copilot.domain.CopilotType;

import java.util.List;
import java.util.Map;

public final class DefaultCapabilities {

    private DefaultCapabilities() {}

    public static Capability searchProducts() {
        return new Capability(
            "SEARCH_PRODUCTS",
            "Search Products",
            "Search and filter products in the SporeKart catalog",
            new CopilotType[]{CopilotType.CUSTOMER, CopilotType.GROWER, CopilotType.ADMIN},
            Map.of("query", "string", "category", "string", "page", "integer", "size", "integer"),
            Map.of("results", "array", "totalCount", "integer", "page", "integer")
        );
    }

    public static Capability placeOrders() {
        return new Capability(
            "PLACE_ORDERS",
            "Place Orders",
            "Create and submit customer orders",
            new CopilotType[]{CopilotType.CUSTOMER, CopilotType.ADMIN},
            Map.of("productId", "string", "quantity", "integer", "shippingAddress", "object"),
            Map.of("orderId", "string", "status", "string", "estimatedDelivery", "string")
        );
    }

    public static Capability recommendProducts() {
        return new Capability(
            "RECOMMEND_PRODUCTS",
            "Recommend Products",
            "Get personalized product recommendations based on user preferences",
            new CopilotType[]{CopilotType.CUSTOMER, CopilotType.GROWER, CopilotType.MARKETING},
            Map.of("userId", "string", "category", "string", "limit", "integer"),
            Map.of("recommendations", "array", "reason", "string")
        );
    }

    public static Capability answerFaqs() {
        return new Capability(
            "ANSWER_FAQS",
            "Answer FAQs",
            "Answer frequently asked questions from the knowledge base",
            new CopilotType[]{CopilotType.CUSTOMER},
            Map.of("question", "string", "category", "string"),
            Map.of("answer", "string", "confidence", "number", "source", "string")
        );
    }

    public static Capability trainingSearch() {
        return new Capability(
            "TRAINING_SEARCH",
            "Training Search",
            "Search training modules and learning resources",
            new CopilotType[]{CopilotType.TRAINER, CopilotType.CUSTOMER, CopilotType.GROWER},
            Map.of("query", "string", "level", "string", "topic", "string"),
            Map.of("results", "array", "totalCount", "integer")
        );
    }

    public static Capability enrollmentAssistance() {
        return new Capability(
            "ENROLLMENT_ASSISTANCE",
            "Enrollment Assistance",
            "Help users enroll in training programs and courses",
            new CopilotType[]{CopilotType.TRAINER, CopilotType.CUSTOMER},
            Map.of("courseId", "string", "userId", "string"),
            Map.of("enrollmentId", "string", "status", "string", "startDate", "string")
        );
    }

    public static Capability inventoryLookup() {
        return new Capability(
            "INVENTORY_LOOKUP",
            "Inventory Lookup",
            "Check product inventory levels and availability",
            new CopilotType[]{CopilotType.GROWER, CopilotType.ADMIN, CopilotType.OPERATIONS, CopilotType.CUSTOMER},
            Map.of("productId", "string", "warehouseId", "string"),
            Map.of("available", "integer", "reserved", "integer", "location", "string")
        );
    }

    public static Capability analytics() {
        return new Capability(
            "ANALYTICS",
            "Analytics",
            "Run analytical queries on business data",
            new CopilotType[]{CopilotType.ADMIN, CopilotType.BUSINESS_INTELLIGENCE, CopilotType.EXECUTIVE, CopilotType.MARKETING},
            Map.of("metric", "string", "dimensions", "array", "filters", "object", "timeRange", "object"),
            Map.of("data", "array", "summary", "object", "generatedAt", "string")
        );
    }

    public static Capability reporting() {
        return new Capability(
            "REPORTING",
            "Reporting",
            "Generate and export business reports",
            new CopilotType[]{CopilotType.ADMIN, CopilotType.BUSINESS_INTELLIGENCE, CopilotType.EXECUTIVE},
            Map.of("reportType", "string", "parameters", "object", "format", "string"),
            Map.of("reportId", "string", "downloadUrl", "string", "generatedAt", "string")
        );
    }

    public static Capability contentGeneration() {
        return new Capability(
            "CONTENT_GENERATION",
            "Content Generation",
            "Generate marketing and communication content",
            new CopilotType[]{CopilotType.MARKETING, CopilotType.ADMIN},
            Map.of("contentType", "string", "topic", "string", "tone", "string", "length", "string"),
            Map.of("content", "string", "suggestions", "array")
        );
    }

    public static Capability knowledgeSearch() {
        return new Capability(
            "KNOWLEDGE_SEARCH",
            "Knowledge Search",
            "Search the SporeKart knowledge base and documentation",
            new CopilotType[]{
                CopilotType.CUSTOMER, CopilotType.GROWER, CopilotType.TRAINER, CopilotType.ADMIN,
                CopilotType.BUSINESS_INTELLIGENCE, CopilotType.MARKETING, CopilotType.OPERATIONS,
                CopilotType.EXECUTIVE, CopilotType.DEVELOPER
            },
            Map.of("query", "string", "scope", "string", "maxResults", "integer"),
            Map.of("results", "array", "totalCount", "integer")
        );
    }

    public static Capability workflowSuggestions() {
        return new Capability(
            "WORKFLOW_SUGGESTIONS",
            "Workflow Suggestions",
            "Suggest automated workflows based on context",
            new CopilotType[]{
                CopilotType.ADMIN, CopilotType.OPERATIONS, CopilotType.DEVELOPER, CopilotType.EXECUTIVE
            },
            Map.of("workflowType", "string", "context", "object"),
            Map.of("suggestions", "array", "automationRate", "number")
        );
    }

    public static Capability orderTracking() {
        return new Capability(
            "ORDER_TRACKING",
            "Order Tracking",
            "Track customer order status and history",
            new CopilotType[]{CopilotType.CUSTOMER, CopilotType.ADMIN, CopilotType.OPERATIONS},
            Map.of("orderId", "string", "includeHistory", "boolean"),
            Map.of("orderId", "string", "status", "string", "history", "array", "estimatedDelivery", "string")
        );
    }

    public static Capability shipmentStatus() {
        return new Capability(
            "SHIPMENT_STATUS",
            "Shipment Status",
            "Check real-time shipment tracking status",
            new CopilotType[]{CopilotType.CUSTOMER, CopilotType.ADMIN, CopilotType.OPERATIONS},
            Map.of("trackingNumber", "string", "carrier", "string"),
            Map.of("trackingNumber", "string", "status", "string", "location", "string", "estimatedDelivery", "string")
        );
    }

    public static Capability cropAdvisory() {
        return new Capability(
            "CROP_ADVISORY",
            "Crop Advisory",
            "Provide agricultural advice and crop management recommendations",
            new CopilotType[]{CopilotType.GROWER},
            Map.of("cropType", "string", "region", "string", "season", "string", "issue", "string"),
            Map.of("advisory", "string", "recommendations", "array", "riskFactors", "array")
        );
    }

    public static List<Capability> all() {
        return List.of(
            searchProducts(),
            placeOrders(),
            recommendProducts(),
            answerFaqs(),
            trainingSearch(),
            enrollmentAssistance(),
            inventoryLookup(),
            analytics(),
            reporting(),
            contentGeneration(),
            knowledgeSearch(),
            workflowSuggestions(),
            orderTracking(),
            shipmentStatus(),
            cropAdvisory()
        );
    }
}
