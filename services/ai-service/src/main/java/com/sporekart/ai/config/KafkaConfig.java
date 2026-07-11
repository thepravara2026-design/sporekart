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
}
