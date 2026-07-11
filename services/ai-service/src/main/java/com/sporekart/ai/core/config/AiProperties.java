package com.sporekart.ai.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai")
public class AiProperties {
    private boolean enabled = true;
    private String defaultProvider = "MOCK";
    private int maxRetries = 3;
    private Duration requestTimeout = Duration.ofSeconds(30);
    private int maxPromptLength = 32000;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getDefaultProvider() { return defaultProvider; }
    public void setDefaultProvider(String defaultProvider) { this.defaultProvider = defaultProvider; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public Duration getRequestTimeout() { return requestTimeout; }
    public void setRequestTimeout(Duration requestTimeout) { this.requestTimeout = requestTimeout; }
    public int getMaxPromptLength() { return maxPromptLength; }
    public void setMaxPromptLength(int maxPromptLength) { this.maxPromptLength = maxPromptLength; }
}
