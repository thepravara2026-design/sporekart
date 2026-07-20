# SporeKart — Production Secrets Management

**PRR-C05 Status:** ✅ CLOSED — 20-Jul-2026

## Secrets Manager: AWS Secrets Manager

### Secret Hierarchy

All production secrets are stored in AWS Secrets Manager under the `/sporekart/prod/` path:

| Secret Name | Description | Source |
|-------------|-------------|--------|
| `sporekart/prod/supabase-url` | Supabase project URL | `.env.production` |
| `sporekart/prod/supabase-anon-key` | Supabase anonymous key | `.env.production` |
| `sporekart/prod/sentry-dsn` | Sentry DSN for error reporting | `.env.production` |
| `sporekart/prod/stripe-pk` | Stripe publishable key | `.env.production` |
| `sporekart/prod/supabase-service-role` | Supabase service role key (admin) | Vault-only |

### Migration from .env to Secrets Manager

```bash
# Initial seeding
aws secretsmanager create-secret \
    --name sporekart/prod/supabase-url \
    --secret-string "https://your-project.supabase.co"

aws secretsmanager create-secret \
    --name sporekart/prod/supabase-anon-key \
    --secret-string "your-supabase-anon-key"

aws secretsmanager create-secret \
    --name sporekart/prod/sentry-dsn \
    --secret-string "https://your-dsn@sentry.sporekart.com/0"

aws secretsmanager create-secret \
    --name sporekart/prod/stripe-pk \
    --secret-string "pk_live_your_stripe_key"
```

### ECS Integration

Secrets are injected into ECS task definitions as environment variables:

```json
{
  "secrets": [
    { "name": "SUPABASE_URL", "valueFrom": "arn:aws:secretsmanager:us-east-1:xxx:secret:sporekart/prod/supabase-url-xxxxx" },
    { "name": "SUPABASE_ANON_KEY", "valueFrom": "arn:aws:secretsmanager:us-east-1:xxx:secret:sporekart/prod/supabase-anon-key-xxxxx" },
    { "name": "VITE_SENTRY_DSN", "valueFrom": "arn:aws:secretsmanager:us-east-1:xxx:secret:sporekart/prod/sentry-dsn-xxxxx" },
    { "name": "VITE_STRIPE_PUBLISHABLE_KEY", "valueFrom": "arn:aws:secretsmanager:us-east-1:xxx:secret:sporekart/prod/stripe-pk-xxxxx" }
  ]
}
```

### Rotation Policy

| Secret | Rotation | Frequency | Method |
|--------|----------|-----------|--------|
| supabase-url | None | Static | — |
| supabase-anon-key | Recommended | 90 days | Manual via Supabase dashboard |
| sentry-dsn | None | Static | — |
| stripe-pk | None | Static | — |
| supabase-service-role | Required | 90 days | Manual rotation |

### Least Privilege

```hcl
# IAM policy for ECS task role
resource "aws_iam_policy" "secrets_read" {
  name = "sporekart-secrets-read"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ]
        Resource = [
          aws_secretsmanager_secret.supabase_url.arn,
          aws_secretsmanager_secret.supabase_anon_key.arn,
          aws_secretsmanager_secret.sentry_dsn.arn,
          aws_secretsmanager_secret.stripe_pk.arn
        ]
      }
    ]
  })
}
```

## Emergency Access

If AWS Secrets Manager is unavailable, fallback secrets are stored in:
- `.env.production` (encrypted at rest in repository)
- 1Password company vault (team access)
- Printed copy in safe (CEO + CTO only)

## Related

- Terraform secrets resources: `../../infrastructure/terraform/production.tf`
- Production env template: `../../.env.production`
- ECS task definition: `../../infrastructure/terraform/production.tf`
