package com.sporekart.ai.configuration.model.environment;

public enum DeploymentProfile {
    LOCAL,
    DEV,
    TEST,
    STAGE,
    PROD,
    DOCKER,
    CLOUD;

    public static DeploymentProfile fromString(String profile) {
        for (DeploymentProfile p : values()) {
            if (p.name().equalsIgnoreCase(profile)) {
                return p;
            }
        }
        return DEV;
    }

    public boolean isProduction() {
        return this == PROD || this == CLOUD;
    }

    public boolean requiresInfrastructure() {
        return this != LOCAL && this != DOCKER;
    }
}
