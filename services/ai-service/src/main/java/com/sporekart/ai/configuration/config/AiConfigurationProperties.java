package com.sporekart.ai.configuration.config;

import com.sporekart.ai.configuration.domain.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "sporekart.ai")
public record AiConfigurationProperties(
    AIConfiguration ai,
    GatewayConfiguration gateway,
    ProviderConfigurationProperties providers,
    RuntimeConfiguration runtime,
    PromptConfiguration prompt,
    EmbeddingConfiguration embedding,
    SemanticConfiguration semantic,
    ConversationConfiguration conversation,
    MemoryConfiguration memory,
    AnalyticsConfiguration analytics,
    SecurityConfiguration security,
    MonitoringConfiguration monitoring,
    TenantConfiguration tenant
) {
    public AiConfigurationProperties() {
        this(
            AIConfiguration.defaults(),
            GatewayConfiguration.defaults(),
            new ProviderConfigurationProperties(),
            RuntimeConfiguration.defaults(),
            PromptConfiguration.defaults(),
            EmbeddingConfiguration.defaults(),
            SemanticConfiguration.defaults(),
            ConversationConfiguration.defaults(),
            MemoryConfiguration.defaults(),
            AnalyticsConfiguration.defaults(),
            SecurityConfiguration.defaults(),
            MonitoringConfiguration.defaults(),
            null
        );
    }
}
