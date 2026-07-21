# Secret Management Guide

## Overview

The Secret Abstraction Layer provides a consistent interface for resolving secrets from multiple providers without coupling application code to any specific secret store.

## Supported Providers

| Provider | Class | Status |
|----------|-------|--------|
| AWS Secrets Manager | `SecretProvider` (interface) | Abstraction ready |
| Azure Key Vault | `SecretProvider` (interface) | Abstraction ready |
| Hashicorp Vault | `SecretProvider` (interface) | Abstraction ready |
| Google Secret Manager | `SecretProvider` (interface) | Abstraction ready |
| Docker Secrets | `SecretProvider` (interface) | Abstraction ready |
| Environment Variables | `SecretProvider` (interface) | Abstraction ready |

## Adding a Secret Provider

1. Implement `SecretProvider` interface
2. Register in Spring context as `@Component`
3. Set `sporekart.ai.security.secret-provider-type` to the provider name
4. Add secret references in configuration

## Secret Reference Example

```java
SecretReference apiKey = SecretReference.of("ai.provider.openai.api-key", SecretProviderType.ENVIRONMENT_VARIABLES)
    .withDescription("OpenAI API Key")
    .withFallbackEnvironmentVariable("AI_OPENAI_API_KEY");
```

## Security Principles

- No secrets stored in source code
- No secrets in version control
- No secrets in log output
- No secrets in error messages
- Secrets encrypted at rest
- Secrets transmitted over TLS only
- Secret access audited
- Secret rotation supported
