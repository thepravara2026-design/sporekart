package com.sporekart.ai.configuration.model.environment;

public enum Environment {
    LOCAL("local", false, false, false),
    DEV("dev", false, false, false),
    TEST("test", false, false, false),
    STAGE("stage", false, true, false),
    PROD("prod", true, true, true),
    DOCKER("docker", false, false, false),
    CLOUD("cloud", true, true, true);

    private final String profile;
    private final boolean production;
    private final boolean requiresSecrets;
    private final boolean requiresSsl;

    Environment(String profile, boolean production, boolean requiresSecrets, boolean requiresSsl) {
        this.profile = profile;
        this.production = production;
        this.requiresSecrets = requiresSecrets;
        this.requiresSsl = requiresSsl;
    }

    public String profile() { return profile; }
    public boolean isProduction() { return production; }
    public boolean requiresSecrets() { return requiresSecrets; }
    public boolean requiresSsl() { return requiresSsl; }

    public static Environment fromProfile(String profile) {
        for (Environment env : values()) {
            if (env.profile.equalsIgnoreCase(profile)) {
                return env;
            }
        }
        return DEV;
    }
}
