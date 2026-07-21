package com.sporekart.ai.configuration.model.secret;

public enum SecretProviderType {
    AWS_SECRETS_MANAGER,
    AZURE_KEY_VAULT,
    HASHICORP_VAULT,
    GOOGLE_SECRET_MANAGER,
    DOCKER_SECRETS,
    ENVIRONMENT_VARIABLES,
    SPRING_CONFIG
}
