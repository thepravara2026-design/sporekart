package com.sporekart.copilot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "copilot")
public class CopilotServiceConfig {

    private int maxSessionDurationMinutes = 60;
    private int maxMessageLength = 4000;
    private boolean streamingEnabled = true;

    public int getMaxSessionDurationMinutes() {
        return maxSessionDurationMinutes;
    }

    public void setMaxSessionDurationMinutes(int maxSessionDurationMinutes) {
        this.maxSessionDurationMinutes = maxSessionDurationMinutes;
    }

    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    public boolean isStreamingEnabled() {
        return streamingEnabled;
    }

    public void setStreamingEnabled(boolean streamingEnabled) {
        this.streamingEnabled = streamingEnabled;
    }
}
