package com.sporekart.ai.conversation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.conversation")
public class ConversationConfig {

    private int sessionTtlHours = 24;
    private int messageLimit = 1000;
    private int contextSize = 10;
    private Duration cacheTtl = Duration.ofMinutes(30);
    private boolean streamingEnabled = true;

    public int getSessionTtlHours() { return sessionTtlHours; }
    public void setSessionTtlHours(int sessionTtlHours) { this.sessionTtlHours = sessionTtlHours; }
    public int getMessageLimit() { return messageLimit; }
    public void setMessageLimit(int messageLimit) { this.messageLimit = messageLimit; }
    public int getContextSize() { return contextSize; }
    public void setContextSize(int contextSize) { this.contextSize = contextSize; }
    public Duration getCacheTtl() { return cacheTtl; }
    public void setCacheTtl(Duration cacheTtl) { this.cacheTtl = cacheTtl; }
    public boolean isStreamingEnabled() { return streamingEnabled; }
    public void setStreamingEnabled(boolean streamingEnabled) { this.streamingEnabled = streamingEnabled; }
}
