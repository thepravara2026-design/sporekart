# Condition C05 — Secrets Management

**PRR Condition:** PRR-C05 — Implement secrets management  
**Priority:** HIGH  
**Status:** ✅ CLOSED — 20-Jul-2026

---

## Root Cause

Production secrets were stored in plaintext `.env.production` file. No secrets manager was configured. ECS task definitions could not reference secrets securely.

## Required Operational Action

1. Configure AWS Secrets Manager for all production secrets:
   - `sporekart/prod/supabase-url`
   - `sporekart/prod/supabase-anon-key`
   - `sporekart/prod/sentry-dsn`
   - `sporekart/prod/stripe-pk`
   - `sporekart/prod/supabase-service-role`
2. Configure ECS task definition to inject secrets from Secrets Manager
3. Define IAM policy for ECS task role with `secretsmanager:GetSecretValue`
4. Document rotation policy and emergency access procedure

## Evidence

| Artifact | Description |
|----------|-------------|
| `infrastructure/secrets/README.md` | Secrets management configuration, rotation, emergency access |

## Validation

```bash
# Verify secrets exist
aws secretsmanager list-secrets --query "SecretList[?contains(Name, 'sporekart/prod')].[Name]"

# Verify ECS task can read secrets (requires task execution role)
aws ecs describe-task-definition --task-definition sporekart-web-app --query "taskDefinition.containerDefinitions[0].secrets"
```

## Rotation

| Secret | Rotation | Period |
|--------|----------|--------|
| supabase-url | None | Static |
| supabase-anon-key | Manual | 90 days |
| sentry-dsn | None | Static |
| stripe-pk | None | Static |
| supabase-service-role | Manual | 90 days |

**Closure Verification:** AWS Secrets Manager integration complete. ECS task definition configured for secret injection. IAM least privilege policy defined. Emergency access documented.
