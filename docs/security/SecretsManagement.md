# Secrets Management

## Principle

All secrets are injected via environment variables at runtime. No hardcoded secrets, no placeholder defaults, no tracked `.env` files.

## Environment Variables

### Critical Secrets (Fail-Fast)

These secrets have **no defaults**. The application fails immediately at startup if any are missing.

| Variable                | Purpose                     | Source                      |
|-------------------------|-----------------------------|-----------------------------|
| `JWT_SECRET`            | HMAC-SHA256 signing key     | Minimum 32 bytes            |
| `DB_PASSWORD`           | Database connection         | Per-environment             |
| `DB_URL`                | JDBC connection string      | Per-environment             |
| `DB_USERNAME`           | Database user               | Per-environment             |
| `AWS_ACCESS_KEY_ID`     | AWS SDK credentials         | AWS Secrets Manager         |
| `AWS_SECRET_ACCESS_KEY` | AWS SDK credentials         | AWS Secrets Manager         |

### Non-Critical Config (Safe Defaults Exist)

| Variable                  | Default            |
|---------------------------|--------------------|
| `SERVER_PORT`             | `8080`             |
| `REDIS_HOST`              | `localhost`        |
| `REDIS_PORT`              | `6379`             |

## .gitignore Rules

```
# Environment files
*.env
.env.*
!.env.example
```

## .env.example Template

```env
JWT_SECRET=
DB_URL=jdbc:postgresql://localhost:5432/sporekart
DB_USERNAME=postgres
DB_PASSWORD=
```

Values that are empty (`JWT_SECRET`, `DB_PASSWORD`) trigger fail-fast validation. Values with safe defaults (`DB_URL`, `DB_USERNAME`) are used as-is.

## AWS Secrets Manager Integration (Target)

For production, secrets are resolved in order:

1. AWS Secrets Manager (primary)
2. Environment variables (fallback for local dev)

The `secretsmanager` Spring profile enables automatic secret resolution via `AwsSecretsManagerPropertySource`.

## Rotation

- JWT secret rotation: supported via `SecretsManagerRotationFilter` (multi-version keys)
- Database credentials: handled externally via AWS RDS rotation or manual secret update + restart