package com.sporekart.ai.usagetracking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sporekart.ai.usage-tracking")
public class UsageTrackingConfig {

    private boolean enabled = true;
    private int defaultListLimit = 100;
    private int topLimit = 10;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getDefaultListLimit() {
        return defaultListLimit;
    }

    public void setDefaultListLimit(int defaultListLimit) {
        this.defaultListLimit = defaultListLimit;
    }

    public int getTopLimit() {
        return topLimit;
    }

    public void setTopLimit(int topLimit) {
        this.topLimit = topLimit;
    }
}
