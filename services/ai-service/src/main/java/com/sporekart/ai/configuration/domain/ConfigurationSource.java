package com.sporekart.ai.configuration.domain;

public enum ConfigurationSource {
    APPLICATION_YML,
    ENVIRONMENT_VARIABLE,
    SECRET_PROVIDER,
    SYSTEM_PROPERTY,
    CONFIG_SERVER,
    DATABASE,
    RUNTIME_OVERRIDE,
    TENANT_OVERRIDE,
    DEFAULT
}
