package com.sporekart.ai.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic aiGatewayEventsTopic() {
        return TopicBuilder.name("ai-gateway-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic warehouseEventsTopic() {
        return TopicBuilder.name("warehouse-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic supplierEventsTopic() {
        return TopicBuilder.name("supplier-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic procurementEventsTopic() {
        return TopicBuilder.name("procurement-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic erpIntegrationEventsTopic() {
        return TopicBuilder.name("erp-integration-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic inventorySyncEventsTopic() {
        return TopicBuilder.name("inventory-sync-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic financeEventsTopic() {
        return TopicBuilder.name("finance-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic gstEventsTopic() {
        return TopicBuilder.name("gst-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic aiProviderEventsTopic() {
        return TopicBuilder.name("ai-provider-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic knowledgeEventsTopic() {
        return TopicBuilder.name("knowledge-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic aiPromptEventsTopic() {
        return TopicBuilder.name("ai-prompt-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic semanticEventsTopic() {
        return TopicBuilder.name("semantic-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic conversationEventsTopic() {
        return TopicBuilder.name("conversation-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic workflowEventsTopic() {
        return TopicBuilder.name("workflow-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic contentEventsTopic() {
        return TopicBuilder.name("content-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic assistantEventsTopic() {
        return TopicBuilder.name("assistant-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic governanceEventsTopic() {
        return TopicBuilder.name("governance-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic policyEventsTopic() {
        return TopicBuilder.name("policy-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic decisionEventsTopic() {
        return TopicBuilder.name("decision-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic approvalEventsTopic() {
        return TopicBuilder.name("approval-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic complianceEventsTopic() {
        return TopicBuilder.name("compliance-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic riskEventsTopic() {
        return TopicBuilder.name("risk-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic analyticsEventsTopic() {
        return TopicBuilder.name("analytics-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminEventsTopic() {
        return TopicBuilder.name("admin-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic automationEventsTopic() {
        return TopicBuilder.name("automation-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic providerRegistryEventsTopic() {
        return TopicBuilder.name("provider-registry-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic promptRegistryEventsTopic() {
        return TopicBuilder.name("prompt-registry-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic knowledgeRegistryEventsTopic() {
        return TopicBuilder.name("knowledge-registry-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic usageTrackingEventsTopic() {
        return TopicBuilder.name("usage-tracking-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic configRegistryEventsTopic() {
        return TopicBuilder.name("config-registry-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventCatalogEventsTopic() {
        return TopicBuilder.name("event-catalog-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic apiRegistryEventsTopic() {
        return TopicBuilder.name("api-registry-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic capabilityEventsTopic() {
        return TopicBuilder.name("capability-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
